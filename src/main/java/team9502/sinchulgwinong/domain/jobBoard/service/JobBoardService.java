package team9502.sinchulgwinong.domain.jobBoard.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.jobBoard.dto.request.JobBoardRequestDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.entity.BoardImage;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;
import team9502.sinchulgwinong.domain.jobBoard.repository.BoardImageRepository;
import team9502.sinchulgwinong.domain.jobBoard.repository.JobBoardRepository;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobBoardService {

    @Value("${S3_NAME}")
    private String bucketName;

    private final JobBoardRepository jobBoardRepository;
    private final CompanyUserRepository companyUserRepository;
    private final BoardImageRepository boardImageRepository;
    private final PointService pointService;
    private final AmazonS3Client amazonS3Client;

    @Transactional
    public JobBoardResponseDTO createJobBoard(
            Long cpUserID,
            JobBoardRequestDTO jobBoardRequestDTO,
            List<MultipartFile> multipartFile){

        validation(jobBoardRequestDTO);

        CompanyUser companyUser = companyUserRepository.findById(cpUserID)
                .orElseThrow(()-> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

        JobBoard jobBoard = new JobBoard();

        jobBoard.setCompanyUser(companyUser);
        jobBoard.setJobTitle(jobBoardRequestDTO.getJobTitle());
        jobBoard.setJobContent(jobBoardRequestDTO.getJobContent());
        jobBoard.setJobStartDate(jobBoardRequestDTO.getJobStartDate());
        jobBoard.setJobEndDate(jobBoardRequestDTO.getJobEndDate());
        jobBoard.setSalaryAmount(jobBoardRequestDTO.getSalaryAmount());
        jobBoard.setSex(jobBoardRequestDTO.getSex());
        jobBoard.setAddress(jobBoardRequestDTO.getAddress());
        jobBoard.setJobStatus(jobBoardRequestDTO.getJobStatus());
        jobBoard.setSalaryType(jobBoardRequestDTO.getSalaryType());


        // 실제로 파일 데이터를 포함하고 있는 파일만 처리
        List<MultipartFile> validFiles = multipartFile.stream()
                .filter(multipartFiles -> !multipartFiles.isEmpty())
                .collect(Collectors.toList());

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
            objectMetadata.setContentLength(multipartFile.getInputStream().available());

            amazonS3Client.putObject(bucketName, filename, multipartFile.getInputStream(), objectMetadata);
            String accessUrl = amazonS3Client.getUrl(bucketName, filename).toString();
            boardImage.setAccessUrl(accessUrl);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boardImageRepository.save(boardImage);

        return boardImage;
    }

    @Transactional(readOnly = true)
    public List<JobBoardResponseDTO> getAllJobBoards(){

        return jobBoardRepository.findAll().stream()
                .map(JobBoardResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JobBoardResponseDTO getJobBoardById(Long jobBoardId){

        JobBoard jobBoard = jobBoardRepository.findById(jobBoardId)
                .orElseThrow(()-> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        return new JobBoardResponseDTO(jobBoard);
    }

    @Transactional
    public JobBoardResponseDTO updateJobBoard(
            Long cpUserId,
            Long jobBoardId,
            JobBoardRequestDTO jobBoardRequestDTO,
            List<MultipartFile> multipartFile){

        JobBoard jobBoard = jobBoardRepository.findById(jobBoardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        if (!jobBoard.getCompanyUser().getCpUserId().equals(cpUserId)) {
            throw new ApiException(ErrorCode.FORBIDDEN_WORK);
        }

        validation(jobBoardRequestDTO);

        jobBoard.setJobTitle(jobBoardRequestDTO.getJobTitle());
        jobBoard.setJobContent(jobBoardRequestDTO.getJobContent());
        jobBoard.setModifiedAt(LocalDateTime.now());

        // 실제로 파일 데이터를 포함하고 있는 파일만 처리
        List<MultipartFile> validFiles = multipartFile.stream()
                .filter(multipartFiles -> !multipartFiles.isEmpty())
                .collect(Collectors.toList());

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
    public void deleteJobBoard(Long cpUserId, Long jobBoardId){

        JobBoard jobBoard = jobBoardRepository.findById(jobBoardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        if (!jobBoard.getCompanyUser().getCpUserId().equals(cpUserId)) {
            throw new ApiException(ErrorCode.FORBIDDEN_WORK);
        }

        for (BoardImage boardImage : jobBoard.getBoardImage()) {

            DeleteObjectRequest request = new DeleteObjectRequest(bucketName, boardImage.getStoredName());
            amazonS3Client.deleteObject(request); // S3에서 이미지 삭제
        }

        jobBoardRepository.delete(jobBoard);
    }


    private void validation(JobBoardRequestDTO jobBoardRequestDTO) {

        if (jobBoardRequestDTO.getJobTitle().isEmpty()) {
            throw new ApiException(ErrorCode.TITLE_REQUIRED);
        }
        if (jobBoardRequestDTO.getJobContent().isEmpty()) {
            throw new ApiException(ErrorCode.CONTENT_REQUIRED);
        }
        if (jobBoardRequestDTO.getJobTitle().length() > 100) {
            throw new ApiException(ErrorCode.TITLE_TOO_LONG);
        }
        if (jobBoardRequestDTO.getJobContent().length() > 1000) {
            throw new ApiException(ErrorCode.CONTENT_TOO_LONG);
        }
    }

}
