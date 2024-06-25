package team9502.sinchulgwinong.domain.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.category.dto.request.JobBoardCategoryRequestDTO;
import team9502.sinchulgwinong.domain.category.service.JobBoardCategoryService;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardListResponseDTO;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;

import java.util.Set;

import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_READ_ALL_CATEGORY_JOB_BOARD;
import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_READ_CATEGORY_JOB_BOARD;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-boards")
public class JobBoardCategoryController {

    private final JobBoardCategoryService jobBoardCategoryService;

    @GetMapping("/region-name")
    public ResponseEntity<GlobalApiResponse<Set<String>>> getAllRegionName() {

        return ResponseEntity.status(SUCCESS_READ_CATEGORY_JOB_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_CATEGORY_JOB_BOARD.getMessage(),
                                jobBoardCategoryService.getAllRegionName()
                        )
                );
    }

    @GetMapping("/sub-region-name")
    public ResponseEntity<GlobalApiResponse<Set<String>>> getAllSubRegionName(
            @RequestParam("region-name") String regionName) {

        Set<String> localities = jobBoardCategoryService.getAllSubRegionName(regionName);

        return ResponseEntity.status(SUCCESS_READ_CATEGORY_JOB_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_CATEGORY_JOB_BOARD.getMessage(),
                                localities
                        )
                );
    }

    @GetMapping("/locality-name")
    public ResponseEntity<GlobalApiResponse<Set<String>>> getAllLocalityName(
            @RequestParam("region-name") String regionName,
            @RequestParam("sub-region-name") String subRegionName) {

        Set<String> localities = jobBoardCategoryService.getAllLocalityName(regionName, subRegionName);

        return ResponseEntity.status(SUCCESS_READ_CATEGORY_JOB_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_CATEGORY_JOB_BOARD.getMessage(),
                                localities
                        )
                );
    }

    @GetMapping("/locality-category")
    public ResponseEntity<GlobalApiResponse<JobBoardListResponseDTO>> getAllLocalityCategory(
            @RequestBody @Valid JobBoardCategoryRequestDTO jobBoardCategoryRequestDTO,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        JobBoardListResponseDTO jobBoardListResponseDTO =
                jobBoardCategoryService.getAllLocalityCategory(jobBoardCategoryRequestDTO, page, size);

        return ResponseEntity.status(SUCCESS_READ_ALL_CATEGORY_JOB_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_CATEGORY_JOB_BOARD.getMessage(),
                                jobBoardListResponseDTO
                        )
                );

    }
}
