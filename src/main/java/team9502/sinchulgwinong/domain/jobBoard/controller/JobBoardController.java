package team9502.sinchulgwinong.domain.jobBoard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team9502.sinchulgwinong.domain.jobBoard.dto.request.JobBoardRequestDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.request.JobBoardUpdateRequestDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardListResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.service.JobBoardService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import java.util.List;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-boards")
@PreAuthorize("hasAuthority('ROLE_COMPANY')")
public class JobBoardController {

    private final JobBoardService jobBoardService;

    @PostMapping
    public ResponseEntity<GlobalApiResponse<JobBoardResponseDTO>> createJobBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(required = false, name = "images") List<MultipartFile> images,
            @RequestPart(name = "request") @Valid JobBoardRequestDTO jobBoardRequestDTO) {

        JobBoardResponseDTO jobBoardResponseDTO = jobBoardService.createJobBoard(userDetails.getCpUserId(), jobBoardRequestDTO, images);

        return ResponseEntity.status(SUCCESS_CREATE_JOB_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CREATE_JOB_BOARD.getMessage(),
                                jobBoardResponseDTO
                        )
                );
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse<JobBoardListResponseDTO>> getAllJobBoards() {

        JobBoardListResponseDTO jobBoardListResponseDTO = jobBoardService.getAllJobBoards();

        return ResponseEntity.status(SUCCESS_READ_ALL_JOB_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_JOB_BOARD.getMessage(),
                                jobBoardListResponseDTO
                        )
                );
    }

    @GetMapping("/{jobBoardId}")
    public ResponseEntity<GlobalApiResponse<JobBoardResponseDTO>> getJobBoardById(
            @PathVariable("jobBoardId") Long jobBoardId) {

        JobBoardResponseDTO jobBoardResponseDTO = jobBoardService.getJobBoardById(jobBoardId);

        return ResponseEntity.status(SUCCESS_READ_JOB_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_JOB_BOARD.getMessage(),
                                jobBoardResponseDTO
                        )
                );
    }

    @PatchMapping("/{jobBoardId}")
    public ResponseEntity<GlobalApiResponse<JobBoardResponseDTO>> updateJobBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("jobBoardId") Long jobBoardId,
            @RequestPart(required = false, name = "images") List<MultipartFile> images,
            @RequestPart(name = "request") @Valid JobBoardUpdateRequestDTO jobBoardUpdateRequestDTO) {

        JobBoardResponseDTO jobBoardResponseDTO = jobBoardService.updateJobBoard(userDetails.getCpUserId(), jobBoardId, jobBoardUpdateRequestDTO, images);

        return ResponseEntity.status(SUCCESS_UPDATE_JOB_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_UPDATE_JOB_BOARD.getMessage(),
                                jobBoardResponseDTO
                        )
                );
    }

    @DeleteMapping("/{jobBoardId}")
    public ResponseEntity<GlobalApiResponse<Void>> deleteJobBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("jobBoardId") Long jobBoardId) {

        jobBoardService.deleteJobBoard(userDetails.getCpUserId(), jobBoardId);

        return ResponseEntity.status(SUCCESS_DELETE_JOB_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_DELETE_JOB_BOARD.getMessage(),
                                null
                        )
                );
    }

    @GetMapping("/my-job-boards")
    public ResponseEntity<GlobalApiResponse<List<JobBoardResponseDTO>>> getAllMyJobBoards(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<JobBoardResponseDTO> jobBoardResponseDTOS = jobBoardService.getAllMyJobBoards(userDetails.getCpUserId());

        return ResponseEntity.status(SUCCESS_READ_ALL_MY_JOB_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_MY_JOB_BOARD.getMessage(),
                                jobBoardResponseDTOS
                        )
                );
    }

    @PostMapping("/{jobBoardId}/ad-job-boards")
    public ResponseEntity<GlobalApiResponse<Void>> adJobBoards(
            @PathVariable("jobBoardId") Long jobBoardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        jobBoardService.adJobBoards(jobBoardId, userDetails.getCpUserId());

        return ResponseEntity.status(SUCCESS_AD_JOB_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_AD_JOB_BOARD.getMessage(),
                                null
                        )
                );
    }

    @GetMapping("/ad-job-boards")
    public ResponseEntity<GlobalApiResponse<List<JobBoardResponseDTO>>> getAllAdJobBoards() {

        List<JobBoardResponseDTO> jobBoardResponseDTOS = jobBoardService.getAllAdJobBoards();

        return ResponseEntity.status(SUCCESS_READ_AD_JOB_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_AD_JOB_BOARD.getMessage(),
                                jobBoardResponseDTOS
                        )
                );
    }

}
