package team9502.sinchulgwinong.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.category.dto.request.JobCategoryRequestDTO;
import team9502.sinchulgwinong.domain.category.dto.request.JobLocalityCategoryRequestDTO;
import team9502.sinchulgwinong.domain.category.entity.JobCategory;
import team9502.sinchulgwinong.domain.category.entity.Locality;
import team9502.sinchulgwinong.domain.category.repository.JobCategoryRepository;
import team9502.sinchulgwinong.domain.category.repository.LocalityRepository;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardListResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;
import team9502.sinchulgwinong.domain.jobBoard.repository.JobBoardRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobBoardCategoryService {

    private final LocalityRepository localityRepository;
    private final JobBoardRepository jobBoardRepository;
    private final JobCategoryRepository jobCategoryRepository;

    @Transactional(readOnly = true)
    public Set<String> getAllRegionName() {

        return localityRepository.findDistinctRegionNames();
    }

    @Transactional(readOnly = true)
    public Set<String> getAllSubRegionName(String regionName) {

        return localityRepository.findSubRegionNamesByRegionName(regionName);
    }

    @Transactional(readOnly = true)
    public Set<String> getAllLocalityName(String regionName, String subRegionName) {

        return localityRepository.findLocalityNamesByRegionAndSubRegion(regionName, subRegionName);
    }

    @Transactional(readOnly = true)
    public JobBoardListResponseDTO getAllLocalityCategory(
            JobLocalityCategoryRequestDTO jobLocalityCategoryRequestDTO, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Locality locality = localityRepository.findByRegionNameAndSubRegionNameAndLocalityName(
                jobLocalityCategoryRequestDTO.getRegionName(),
                jobLocalityCategoryRequestDTO.getSubRegionName(),
                jobLocalityCategoryRequestDTO.getLocalityName());

        Page<JobBoard> jobBoardPage = jobBoardRepository.findByLocality_LocalityId(locality.getLocalityId(), pageable);

        List<JobBoardResponseDTO> jobBoardResponseDTOS = jobBoardPage.stream()
                .map(JobBoardResponseDTO::new)
                .toList();

        return new JobBoardListResponseDTO(
                jobBoardResponseDTOS,
                jobBoardPage.getTotalElements(),
                jobBoardPage.getNumber(),
                jobBoardPage.getTotalPages(),
                jobBoardPage.getSize());
    }

    @Transactional(readOnly = true)
    public Set<String> getAllMajorCategoryName(){

        return jobCategoryRepository.findMajorCategoryName();
    }

    @Transactional(readOnly = true)
    public Set<String> getAllMinorCategoryName(String minorCategoryName){

        return jobCategoryRepository.findMinorCategoryName(minorCategoryName);
    }

    @Transactional(readOnly = true)
    public JobBoardListResponseDTO getAllJobCategory(
            JobCategoryRequestDTO jobCategoryRequestDTO, int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        JobCategory jobCategory =
                jobCategoryRepository.findByMajorCategoryNameAndMinorCategoryName(
                        jobCategoryRequestDTO.getMajorCategoryName(),
                        jobCategoryRequestDTO.getMinorCategoryName());

        Page<JobBoard> jobBoardPage =
                jobBoardRepository.findByJobCategory_JobCategoryId(jobCategory.getJobCategoryId(), pageable);

        List<JobBoardResponseDTO> jobBoardResponseDTOS = jobBoardPage.stream()
                .map(JobBoardResponseDTO::new)
                .toList();

        return new JobBoardListResponseDTO(
                jobBoardResponseDTOS,
                jobBoardPage.getTotalElements(),
                jobBoardPage.getNumber(),
                jobBoardPage.getTotalPages(),
                jobBoardPage.getSize());
    }
}
