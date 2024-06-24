package team9502.sinchulgwinong.domain.jobBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.jobBoard.dto.request.JobBoardCategoryRequestDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardListResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;
import team9502.sinchulgwinong.domain.jobBoard.entity.Locality;
import team9502.sinchulgwinong.domain.jobBoard.repository.JobBoardRepository;
import team9502.sinchulgwinong.domain.jobBoard.repository.LocalityRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobBoardCategoryService {

    private final LocalityRepository localityRepository;
    private final JobBoardRepository jobBoardRepository;

    @Transactional(readOnly = true)
    public Set<String> getAllRegionName(){

        return localityRepository.findDistinctRegionNames();
    }

    @Transactional(readOnly = true)
    public Set<String> getAllSubRegionName(String regionName){

        return localityRepository.findSubRegionNamesByRegionName(regionName);
    }

    @Transactional(readOnly = true)
    public Set<String> getAllLocalityName(String regionName, String subRegionName){

        return localityRepository.findLocalityNamesByRegionAndSubRegion(regionName,subRegionName);
    }

    @Transactional(readOnly = true)
    public JobBoardListResponseDTO getAllLocalityCategory(
            JobBoardCategoryRequestDTO jobBoardCategoryRequestDTO, int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        Locality locality = localityRepository.findByRegionNameAndSubRegionNameAndLocalityName(
                jobBoardCategoryRequestDTO.getRegionName(),
                jobBoardCategoryRequestDTO.getSubRegionName(),
                jobBoardCategoryRequestDTO.getLocalityName());

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

}
