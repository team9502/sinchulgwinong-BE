package team9502.sinchulgwinong.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.comment.dto.request.CommentRequestDTO;
import team9502.sinchulgwinong.domain.comment.dto.response.CommentResponseDTO;
import team9502.sinchulgwinong.domain.comment.service.CommentService;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import java.util.List;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequestMapping("/comments/{boardId}")
@PreAuthorize("hasAuthority('ROLE_USER')")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<GlobalApiResponse<Object>> commentCreate(
            @PathVariable Long boardId,
            @RequestBody CommentRequestDTO commentRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = (User) userDetails.getUser();

        CommentResponseDTO commentResponseDTO = commentService.createComment(boardId, user, commentRequestDTO);

        return ResponseEntity.status(SUCCESS_CREATE_COMMENT.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CREATE_COMMENT.getMessage(),
                                commentResponseDTO
                        )
                );
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse<List<CommentResponseDTO>>> getAllComment(
            @PathVariable Long boardId) {

        List<CommentResponseDTO> commentResponseDTOS = commentService.getAllComment(boardId);

        return ResponseEntity.status(SUCCESS_READ_ALL_COMMENT.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_COMMENT.getMessage(),
                                commentResponseDTOS
                        )
                );
    }

    @PatchMapping("replies/{commentId}")
    public ResponseEntity<GlobalApiResponse<CommentResponseDTO>> commentUpdate(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CommentRequestDTO commentRequestDTO) {

        User user = (User) userDetails.getUser();

        CommentResponseDTO commentResponseDTO = commentService.updateComment(boardId, commentId, user, commentRequestDTO);

        return ResponseEntity.status(SUCCESS_UPDATE_COMMENT.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_UPDATE_COMMENT.getMessage(),
                                commentResponseDTO
                        )
                );
    }

    @DeleteMapping("replies/{commentId}")
    public ResponseEntity<GlobalApiResponse<Object>> deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = (User) userDetails.getUser();

        commentService.deleteComment(boardId, commentId, user);

        return ResponseEntity.status(SUCCESS_DELETE_COMMENT.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_DELETE_COMMENT.getMessage(),
                                null
                        )
                );
    }

}

