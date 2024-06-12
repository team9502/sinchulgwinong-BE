package team9502.sinchulgwinong.domain.jobBoard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team9502.sinchulgwinong.domain.jobBoard.dto.request.JobBoardRequestDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.service.JobBoardService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import java.util.List;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobBoards")
@PreAuthorize("hasAuthority('ROLE_COMPANY')")
public class JobBoardController {

    private final JobBoardService jobBoardService;

    @PostMapping
    public ResponseEntity<GlobalApiResponse<JobBoardResponseDTO>> createJobBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(required = false, name = "images") List<MultipartFile> images,
            @RequestPart(name = "request")@Valid JobBoardRequestDTO jobBoardRequestDTO) {

        JobBoardResponseDTO jobBoardResponseDTO = jobBoardService.createJobBoard(userDetails.getCpUserId(), jobBoardRequestDTO, images);

        return ResponseEntity.status(SUCCESS_CREATE_JOBBOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CREATE_JOBBOARD.getMessage(),
                                jobBoardResponseDTO
                        )
                );
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse<List<JobBoardResponseDTO>>> getAllJobBoards() {

        List<JobBoardResponseDTO> jobBoardResponseDTOS = jobBoardService.getAllJobBoards();

        return ResponseEntity.status(SUCCESS_READ_ALL_JOBBOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_JOBBOARD.getMessage(),
                                jobBoardResponseDTOS
                        )
                );
    }

    @GetMapping("/{jobBoardId}")
    public ResponseEntity<GlobalApiResponse<JobBoardResponseDTO>> getJobBoardById(
            @PathVariable("jobBoardId") Long jobBoardId) {

        JobBoardResponseDTO jobBoardResponseDTO = jobBoardService.getJobBoardById(jobBoardId);

        return ResponseEntity.status(SUCCESS_READ_JOBBOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_JOBBOARD.getMessage(),
                                jobBoardResponseDTO
                        )
                );
    }

    @PatchMapping("/{jobBoardId}")
    public ResponseEntity<GlobalApiResponse<JobBoardResponseDTO>> updateJobBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("jobBoardId") Long jobBoardId,
            @RequestPart(required = false, name = "images") List<MultipartFile> images,
            @RequestPart(name = "request") @Valid JobBoardRequestDTO jobBoardRequestDTO) {

        JobBoardResponseDTO jobBoardResponseDTO = jobBoardService.updateJobBoard(userDetails.getCpUserId(), jobBoardId, jobBoardRequestDTO, images);

        return ResponseEntity.status(SUCCESS_UPDATE_JOBBOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_UPDATE_JOBBOARD.getMessage(),
                                jobBoardResponseDTO
                        )
                );
    }

    @DeleteMapping("/{jobBoardId}")
    public ResponseEntity<GlobalApiResponse<Void>> deleteJobBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("jobBoardId") Long jobBoardId) {

        jobBoardService.deleteJobBoard(userDetails.getCpUserId(), jobBoardId);

        return ResponseEntity.status(SUCCESS_DELETE_JOBBOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_DELETE_JOBBOARD.getMessage(),
                                null
                        )
                );
    }

}
