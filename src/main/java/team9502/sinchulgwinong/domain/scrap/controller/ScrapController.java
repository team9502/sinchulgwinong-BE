package team9502.sinchulgwinong.domain.scrap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.scrap.dto.response.JobScrapResponseDTO;
import team9502.sinchulgwinong.domain.scrap.dto.response.ScrapResponseDTO;
import team9502.sinchulgwinong.domain.scrap.service.ScrapService;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import java.util.List;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequestMapping("/scraps")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("/boards/{boardId}")
    public ResponseEntity<GlobalApiResponse<Object>> scrapCreateBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

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
    public ResponseEntity<GlobalApiResponse<List<ScrapResponseDTO>>> getAllScraps(

            @AuthenticationPrincipal UserDetailsImpl userDetails){

        User user = (User) userDetails.getUser();

        List<ScrapResponseDTO> scrapResponseDTOS = scrapService.getAllScraps(user);

        return ResponseEntity.status(SUCCESS_READ_ALL_SCRAP.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_SCRAP.getMessage(),
                                scrapResponseDTOS
                        )
                );
    }

    @PostMapping("/jobBoards/{/jobBoardId}")
    public ResponseEntity<GlobalApiResponse<Object>> scrapCreateJobBoard(
            @PathVariable Long jobBoardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

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
        }
        else {
            return ResponseEntity.status(SUCCESS_CREATE_SCRAP.getHttpStatus())
                    .body(
                            GlobalApiResponse.of(
                                    SUCCESS_DELETE_SCRAP.getMessage(),
                                    null
                            )
                    );
    }
}

    @GetMapping("/jobBoards")
    public ResponseEntity<GlobalApiResponse<List<JobScrapResponseDTO>>> getAllJobScraps(
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        User user = (User) userDetails.getUser();

        List<JobScrapResponseDTO> jobScrapResponseDTOS = scrapService.getAllJobBoards(user);

        return ResponseEntity.status(SUCCESS_READ_ALL_SCRAP.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_SCRAP.getMessage(),
                                jobScrapResponseDTOS
                        )
                );
    }

}
