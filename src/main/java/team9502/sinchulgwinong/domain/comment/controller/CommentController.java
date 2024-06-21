package team9502.sinchulgwinong.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.comment.dto.request.CommentRequestDTO;
import team9502.sinchulgwinong.domain.comment.dto.request.CommentUdateRequestDTO;
import team9502.sinchulgwinong.domain.comment.dto.response.CommentListResponseDTO;
import team9502.sinchulgwinong.domain.comment.dto.response.CommentResponseDTO;
import team9502.sinchulgwinong.domain.comment.service.CommentService;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import java.util.List;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequestMapping("/comments")
@PreAuthorize("hasAuthority('ROLE_USER')")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/boards/{boardId}")
    public ResponseEntity<GlobalApiResponse<CommentResponseDTO>> commentCreate(
            @PathVariable("boardId") Long boardId,
            @RequestBody @Valid CommentRequestDTO commentRequestDTO,
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

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<GlobalApiResponse<CommentListResponseDTO>> getAllComment(
            @PathVariable("boardId") Long boardId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        CommentListResponseDTO commentListResponseDTO = commentService.getAllComment(boardId, page, size);

        return ResponseEntity.status(SUCCESS_READ_ALL_COMMENT.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_COMMENT.getMessage(),
                                commentListResponseDTO
                        )
                );
    }

    @PatchMapping("{commentId}/boards/{boardId}")
    public ResponseEntity<GlobalApiResponse<CommentResponseDTO>> commentUpdate(
            @PathVariable("boardId") Long boardId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CommentUdateRequestDTO commentUdateRequestDTO) {

        User user = (User) userDetails.getUser();

        CommentResponseDTO commentResponseDTO = commentService.updateComment(boardId, commentId, user, commentUdateRequestDTO);

        return ResponseEntity.status(SUCCESS_UPDATE_COMMENT.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_UPDATE_COMMENT.getMessage(),
                                commentResponseDTO
                        )
                );
    }

    @DeleteMapping("/{commentId}/boards/{boardId}")
    public ResponseEntity<GlobalApiResponse<Void>> deleteComment(
            @PathVariable("boardId") Long boardId,
            @PathVariable("commentId") Long commentId,
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

    @GetMapping("/my-comments")
    public ResponseEntity<GlobalApiResponse<CommentListResponseDTO>> getAllMyComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "page", defaultValue = "5") int size) {

        User user = (User) userDetails.getUser();

        CommentListResponseDTO commentListResponseDTO = commentService.getAllMyComment(user, page, size);

        return ResponseEntity.status(SUCCESS_READ_ALL_MY_COMMENT.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_MY_COMMENT.getMessage(),
                                commentListResponseDTO
                        )
                );
    }

}

