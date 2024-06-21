package team9502.sinchulgwinong.domain.scrap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.board.dto.response.BoardListResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardListResponseDTO;
import team9502.sinchulgwinong.domain.scrap.dto.response.CpUserScrapListResponseDTO;
import team9502.sinchulgwinong.domain.scrap.service.ScrapService;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequestMapping("/scraps")
@PreAuthorize("hasAuthority('ROLE_USER')")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("/boards/{boardId}")
    public ResponseEntity<GlobalApiResponse<Void>> scrapCreateBoard(
            @PathVariable(("boardId")) Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = (User) userDetails.getUser();

        boolean isCreated = scrapService.scrapCreateBoard(boardId, user);

        if (isCreated) {
            return ResponseEntity.status(SUCCESS_CREATE_SCRAP.getHttpStatus())
                    .body(
                            GlobalApiResponse.of(
                                    SUCCESS_CREATE_SCRAP.getMessage(),
                                    null
                            )
                    );
        } else {
            return ResponseEntity.status(SUCCESS_DELETE_SCRAP.getHttpStatus())
                    .body(
                            GlobalApiResponse.of(
                                    SUCCESS_DELETE_SCRAP.getMessage(),
                                    null
                            )
                    );
        }
    }

    @GetMapping("/boards")
    public ResponseEntity<GlobalApiResponse<BoardListResponseDTO>> getAllScraps(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size) {

        User user = (User) userDetails.getUser();

        BoardListResponseDTO boardListResponseDTO = scrapService.getAllScraps(user, page, size);

        return ResponseEntity.status(SUCCESS_READ_ALL_SCRAP.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_SCRAP.getMessage(),
                                boardListResponseDTO
                        )
                );
    }

    @PostMapping("/job-boards/{jobBoardId}")
    public ResponseEntity<GlobalApiResponse<Void>> scrapCreateJobBoard(
            @PathVariable("jobBoardId") Long jobBoardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = (User) userDetails.getUser();

        boolean isCreated = scrapService.scrapCreateJobBoard(jobBoardId, user);

        if (isCreated) {
            return ResponseEntity.status(SUCCESS_CREATE_SCRAP.getHttpStatus())
                    .body(
                            GlobalApiResponse.of(
                                    SUCCESS_CREATE_SCRAP.getMessage(),
                                    null
                            )
                    );
        } else {
            return ResponseEntity.status(SUCCESS_CREATE_SCRAP.getHttpStatus())
                    .body(
                            GlobalApiResponse.of(
                                    SUCCESS_DELETE_SCRAP.getMessage(),
                                    null
                            )
                    );
        }
    }

    @GetMapping("/job-boards")
    public ResponseEntity<GlobalApiResponse<JobBoardListResponseDTO>> getAllJobScraps(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size) {

        User user = (User) userDetails.getUser();

        JobBoardListResponseDTO jobBoardListResponseDTO = scrapService.getAllJobBoards(user, page, size);

        return ResponseEntity.status(SUCCESS_READ_ALL_SCRAP.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_SCRAP.getMessage(),
                                jobBoardListResponseDTO
                        )
                );
    }

    @PostMapping("/cp-user/{cpUserId}")
    public ResponseEntity<GlobalApiResponse<Void>> cpUserScrapCreate(
            @PathVariable(name = "cpUserId") Long cpUserId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = (User) userDetails.getUser();

        boolean isCreated = scrapService.cpUserScrapCreate(user, cpUserId);

        if (isCreated) {
            return ResponseEntity.status(SUCCESS_CREATE_SCRAP.getHttpStatus())
                    .body(
                            GlobalApiResponse.of(
                                    SUCCESS_CREATE_SCRAP.getMessage(),
                                    null
                            )
                    );
        } else {
            return ResponseEntity.status(SUCCESS_CREATE_SCRAP.getHttpStatus())
                    .body(
                            GlobalApiResponse.of(
                                    SUCCESS_DELETE_SCRAP.getMessage(),
                                    null
                            )
                    );
        }
    }

    @GetMapping("/cp-user")
    public ResponseEntity<GlobalApiResponse<CpUserScrapListResponseDTO>> getAllCpUserScrap(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size) {

        User user = (User) userDetails.getUser();

        CpUserScrapListResponseDTO cpUserScrapListResponseDTO = scrapService.getAllCpUserScrap(user, page, size);

        return ResponseEntity.status(SUCCESS_READ_ALL_SCRAP.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_SCRAP.getMessage(),
                                cpUserScrapListResponseDTO
                        )
                );
    }

}
