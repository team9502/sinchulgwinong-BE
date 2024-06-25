package team9502.sinchulgwinong.domain.jobBoard.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.jobBoard.dto.request.JobBoardRequestDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.request.JobBoardUpdateRequestDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardListResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.entity.*;
import team9502.sinchulgwinong.domain.jobBoard.repository.AdJobBoardRepository;
import team9502.sinchulgwinong.domain.jobBoard.repository.BoardImageRepository;
import team9502.sinchulgwinong.domain.jobBoard.repository.JobBoardRepository;
import team9502.sinchulgwinong.domain.jobBoard.repository.LocalityRepository;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.enums.UpType;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobBoardService {

    @Value("${S3_NAME}")
    private String bucketName;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    private final JobBoardRepository jobBoardRepository;
    private final CompanyUserRepository companyUserRepository;
    private final BoardImageRepository boardImageRepository;
    private final AdJobBoardRepository adJobBoardRepository;
    private final LocalityRepository localityRepository;
    private final PointService pointService;
    private final AmazonS3Client amazonS3Client;

    @Transactional
    public JobBoardResponseDTO createJobBoard(
            Long cpUserId,
            JobBoardRequestDTO jobBoardRequestDTO,
            List<MultipartFile> multipartFile) {

        validation(jobBoardRequestDTO);

        CompanyUser companyUser = companyUserRepository.findById(cpUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

        Locality locality = localityRepository.findByRegionNameAndSubRegionNameAndLocalityName(
                jobBoardRequestDTO.getRegionName(),
                jobBoardRequestDTO.getSubRegionName(),
                jobBoardRequestDTO.getLocalityName());

        JobBoard jobBoard = new JobBoard();

        jobBoard.setCompanyUser(companyUser);
        jobBoard.setLocality(locality);
        jobBoard.setCpName(companyUser.getCpName());
        jobBoard.setJobTitle(jobBoardRequestDTO.getJobTitle());
        jobBoard.setJobContent(jobBoardRequestDTO.getJobContent());
        jobBoard.setJobStartDate(LocalDate.now());
        jobBoard.setJobEndDate(jobBoardRequestDTO.getJobEndDate());
        jobBoard.setSalaryAmount(jobBoardRequestDTO.getSalaryAmount());
        jobBoard.setSex(jobBoardRequestDTO.getSex());
        jobBoard.setAddress(jobBoardRequestDTO.getAddress());
        jobBoard.setJobStatus(JobStatus.JOBOPEN);
        jobBoard.setSalaryType(jobBoardRequestDTO.getSalaryType());


        // 실제로 파일 데이터를 포함하고 있는 파일만 처리
        List<MultipartFile> validFiles = multipartFile.stream()
                .filter(multipartFiles -> !multipartFiles.isEmpty())
                .collect(Collectors.toList());

        // 허용되지 않은 파일 확장자가 있는지 검사
        for (MultipartFile file : validFiles) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !isAllowedExtension(originalFilename)) {
                throw new ApiException(ErrorCode.INVALID_FILE_EXTENSION);
            }
        }

        if (!validFiles.isEmpty()) {
            List<BoardImage> boardImageList = saveBoardImages(validFiles, jobBoard);
            jobBoard.setBoardImage(boardImageList);
        }

        jobBoardRepository.save(jobBoard);

        pointService.earnPoints(companyUser, SpType.JOBS);

        return new JobBoardResponseDTO(jobBoard);
    }

    // 게시글 이미지 저장
    public List<BoardImage> saveBoardImages(List<MultipartFile> multipartFiles, JobBoard jobBoard) {

        List<BoardImage> boardImages = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            BoardImage image = saveImageBo(multipartFile, jobBoard);
            boardImages.add(image);
        }

        return boardImages;
    }

    public BoardImage saveImageBo(MultipartFile multipartFile, JobBoard jobBoard) {

        String originalName = multipartFile.getOriginalFilename();
        BoardImage boardImage = new BoardImage(originalName);
        boardImage.setJobBoard(jobBoard);

        String filename = boardImage.getStoredName();

        try {

            ObjectMetadata objectMetadata = new ObjectMetadata();

            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentDisposition("inline");

            amazonS3Client.putObject(bucketName, filename, multipartFile.getInputStream(), objectMetadata);
            String accessUrl = amazonS3Client.getUrl(bucketName, filename).toString();
            boardImage.setAccessUrl(accessUrl);

        } catch (IOException e) {
            throw new RuntimeException("S3에 업로드 실패 ", e);
        }

        boardImageRepository.save(boardImage);

        return boardImage;
    }

    @Transactional(readOnly = true)
    public JobBoardListResponseDTO getAllJobBoards(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<JobBoard> jobBoardPage = jobBoardRepository.findAll(pageable);

        List<JobBoardResponseDTO> jobBoardResponseDTOS = jobBoardPage.stream()
                .map(JobBoardResponseDTO::new)
                .collect(Collectors.toList());

        return new JobBoardListResponseDTO(
                jobBoardResponseDTOS,
                jobBoardPage.getTotalElements(),
                jobBoardPage.getNumber(),
                jobBoardPage.getTotalPages(),
                jobBoardPage.getSize()
        );
    }

    @Transactional(readOnly = true)
    public JobBoardResponseDTO getJobBoardById(Long jobBoardId) {

        JobBoard jobBoard = jobBoardRepository.findById(jobBoardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        return new JobBoardResponseDTO(jobBoard);
    }

    @Transactional
    public JobBoardResponseDTO updateJobBoard(
            Long cpUserId,
            Long jobBoardId,
            JobBoardUpdateRequestDTO jobBoardUpdateRequestDTO,
            List<MultipartFile> multipartFile) {

        validation(jobBoardUpdateRequestDTO);

        JobBoard jobBoard = jobBoardRepository.findById(jobBoardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        if (!jobBoard.getCompanyUser().getCpUserId().equals(cpUserId)) {
            throw new ApiException(ErrorCode.FORBIDDEN_WORK);
        }

        if (jobBoardUpdateRequestDTO.getJobTitle() != null) {
            jobBoard.setJobTitle(jobBoardUpdateRequestDTO.getJobTitle());
        }
        if (jobBoardUpdateRequestDTO.getJobContent() != null) {
            jobBoard.setJobContent(jobBoardUpdateRequestDTO.getJobContent());
        }
        if (jobBoardUpdateRequestDTO.getJobStartDate() != null) {
            jobBoard.setJobStartDate(jobBoardUpdateRequestDTO.getJobStartDate());
        }
        if (jobBoardUpdateRequestDTO.getJobEndDate() != null) {
            jobBoard.setJobEndDate(jobBoardUpdateRequestDTO.getJobEndDate());
        }
        if (jobBoardUpdateRequestDTO.getSalaryAmount() != null) {
            jobBoard.setSalaryAmount(jobBoardUpdateRequestDTO.getSalaryAmount());
        }
        if (jobBoardUpdateRequestDTO.getSex() != null) {
            jobBoard.setSex(jobBoardUpdateRequestDTO.getSex());
        }
        if (jobBoardUpdateRequestDTO.getAddress() != null) {
            jobBoard.setAddress(jobBoardUpdateRequestDTO.getAddress());
        }
        if (jobBoardUpdateRequestDTO.getJobStatus() != null) {
            jobBoard.setJobStatus(jobBoardUpdateRequestDTO.getJobStatus());
        }
        if (jobBoardUpdateRequestDTO.getSalaryType() != null) {
            jobBoard.setSalaryType(jobBoardUpdateRequestDTO.getSalaryType());
        }
        if(jobBoardUpdateRequestDTO.getRegionName() != null &&
                jobBoardUpdateRequestDTO.getSubRegionName() != null &&
                jobBoardUpdateRequestDTO.getLocalityName() != null) {

            Locality locality = localityRepository.findByRegionNameAndSubRegionNameAndLocalityName(
                    jobBoardUpdateRequestDTO.getRegionName(),
                    jobBoardUpdateRequestDTO.getSubRegionName(),
                    jobBoardUpdateRequestDTO.getLocalityName());

            jobBoard.setLocality(locality);
        }

        // 실제로 파일 데이터를 포함하고 있는 파일만 처리
        List<MultipartFile> validFiles = multipartFile.stream()
                .filter(multipartFiles -> !multipartFiles.isEmpty())
                .collect(Collectors.toList());

        // 허용되지 않은 파일 확장자가 있는지 검사
        for (MultipartFile file : validFiles) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !isAllowedExtension(originalFilename)) {
                throw new ApiException(ErrorCode.INVALID_FILE_EXTENSION);
            }
        }

        if (!validFiles.isEmpty()) {
            List<BoardImage> existingImages = jobBoard.getBoardImage();
            List<BoardImage> newImages = saveBoardImages(validFiles, jobBoard);
            existingImages.addAll(newImages);
            jobBoard.setBoardImage(existingImages);
        }

        jobBoardRepository.save(jobBoard);

        return new JobBoardResponseDTO(jobBoard);
    }

    @Transactional
    public void deleteJobBoard(Long cpUserId, Long jobBoardId) {

        JobBoard jobBoard = jobBoardRepository.findById(jobBoardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        if (!jobBoard.getCompanyUser().getCpUserId().equals(cpUserId)) {
            throw new ApiException(ErrorCode.FORBIDDEN_WORK);
        }

        adJobBoardRepository.deleteByJobBoard_JobBoardId(jobBoardId);

        for (BoardImage boardImage : jobBoard.getBoardImage()) {

            DeleteObjectRequest request = new DeleteObjectRequest(bucketName, boardImage.getStoredName());
            amazonS3Client.deleteObject(request); // S3에서 이미지 삭제
        }

        jobBoardRepository.delete(jobBoard);
    }

    @Transactional(readOnly = true)
    public JobBoardListResponseDTO getAllMyJobBoards(Long cpUserId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<JobBoard> jobBoardPage = jobBoardRepository.findByCompanyUser_CpUserId(cpUserId, pageable);

        List<JobBoardResponseDTO> jobBoardResponseDTOS = jobBoardPage.stream()
                .map(JobBoardResponseDTO::new)
                .collect(Collectors.toList());

        return new JobBoardListResponseDTO(
                jobBoardResponseDTOS,
                jobBoardPage.getTotalElements(),
                jobBoardPage.getNumber(),
                jobBoardPage.getTotalPages(),
                jobBoardPage.getSize());
    }

    @Transactional
    public void adJobBoards(Long jobBoardId, Long cpUserId) {

        CompanyUser companyUser = companyUserRepository.findById(cpUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

        pointService.deductPoints(companyUser, UpType.TOP);

        if (adJobBoardRepository.findByJobBoard_JobBoardId(jobBoardId) != null) {
            throw new ApiException(ErrorCode.JOB_BOARD_ALREADY_AD);
        }

        JobBoard jobBoard = jobBoardRepository.findById(jobBoardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        AdJobBoard adJobBoard = new AdJobBoard();

        adJobBoard.setJobBoard(jobBoard);

        adJobBoardRepository.save(adJobBoard);
    }

    @Transactional(readOnly = true)
    public List<JobBoardResponseDTO> getAllAdJobBoards() {

        return adJobBoardRepository.findAll().stream()
                .map(adJobBoard -> {
                    JobBoard jobBoard = adJobBoard.getJobBoard();
                    return new JobBoardResponseDTO(jobBoard);
                })
                .collect(Collectors.toList());
    }


    private void validation(JobBoardRequestDTO jobBoardRequestDTO) {

        if (jobBoardRequestDTO.getJobTitle().length() > 100) {
            throw new ApiException(ErrorCode.TITLE_TOO_LONG);
        }
        if (jobBoardRequestDTO.getJobContent().length() > 1000) {
            throw new ApiException(ErrorCode.CONTENT_TOO_LONG);
        }
    }

    private void validation(JobBoardUpdateRequestDTO jobBoardUpdateRequestDTO) {

        if (jobBoardUpdateRequestDTO.getJobTitle().length() > 100) {
            throw new ApiException(ErrorCode.TITLE_TOO_LONG);
        }
        if (jobBoardUpdateRequestDTO.getJobContent().length() > 1000) {
            throw new ApiException(ErrorCode.CONTENT_TOO_LONG);
        }
    }

    private boolean isAllowedExtension(String fileName) {

        String fileExtension = "";

        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            fileExtension = fileName.substring(dotIndex + 1).toLowerCase();
        }

        return ALLOWED_EXTENSIONS.contains(fileExtension);
    }
}
