package team9502.sinchulgwinong.domain.comment.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.board.entity.Board;
import team9502.sinchulgwinong.domain.board.repository.BoardRepository;
import team9502.sinchulgwinong.domain.comment.dto.request.CommentRequestDTO;
import team9502.sinchulgwinong.domain.comment.dto.request.CommentUdateRequestDTO;
import team9502.sinchulgwinong.domain.comment.dto.response.CommentListResponseDTO;
import team9502.sinchulgwinong.domain.comment.dto.response.CommentResponseDTO;
import team9502.sinchulgwinong.domain.comment.entity.Comment;
import team9502.sinchulgwinong.domain.comment.repository.CommentRepository;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final PointService pointService;

    @Transactional
    public CommentResponseDTO createComment(Long boardId, User user, CommentRequestDTO commentRequestDTO) {

        validation(commentRequestDTO);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        Comment comment = new Comment();

        comment.setUser(user);
        comment.setBoard(board);
        comment.setCommentContent(commentRequestDTO.getCommentContent());

        commentRepository.save(comment);

        pointService.earnPoints(user, SpType.COMMENT);

        return new CommentResponseDTO(comment);
    }

    @Transactional(readOnly = true)
    public CommentListResponseDTO getAllComment(Long boardId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Comment> commentPage = commentRepository.findByBoard_BoardId(boardId, pageable);

        List<CommentResponseDTO> commentResponseDTOS = commentPage.stream()
                .map(CommentResponseDTO::new)
                .toList();

        return new CommentListResponseDTO(
                commentResponseDTOS,
                commentPage.getTotalElements(),
                commentPage.getNumber(),
                commentPage.getTotalPages(),
                commentPage.getSize());
    }

    @Transactional
    public CommentResponseDTO updateComment(Long boardId, Long commentId, User user, CommentUdateRequestDTO commentUdateRequestDTO) {

        validation(commentUdateRequestDTO);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMMENT_NOT_FOUND));

        if (!user.getUserId().equals(comment.getUser().getUserId())) {
            throw new ApiException(ErrorCode.FORBIDDEN_WORK);
        }

        if (!comment.getBoard().getBoardId().equals(boardId)) {
            throw new ApiException(ErrorCode.BOARD_NOT_FOUND);
        }

        if(commentUdateRequestDTO.getCommentContent() != null)
            comment.setCommentContent(commentUdateRequestDTO.getCommentContent());

        commentRepository.save(comment);

        return new CommentResponseDTO(comment);
    }


    @Transactional
    public void deleteComment(Long boardId, Long commentId, User user) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMMENT_NOT_FOUND));

        if (!user.getUserId().equals(comment.getUser().getUserId())) {
            throw new ApiException(ErrorCode.FORBIDDEN_WORK);
        }

        if (!comment.getBoard().getBoardId().equals(boardId)) {
            throw new ApiException(ErrorCode.BOARD_NOT_FOUND);
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public CommentListResponseDTO getAllMyComment(User user, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Comment> commentPage = commentRepository.findByUser_UserId(user.getUserId(),pageable);

        List<CommentResponseDTO> commentResponseDTOS =
                commentPage.stream()
                .map(CommentResponseDTO::new)
                .toList();

        return new CommentListResponseDTO(
                commentResponseDTOS,
                commentPage.getTotalElements(),
                commentPage.getNumber(),
                commentPage.getTotalPages(),
                commentPage.getSize());
    }

    private void validation(CommentRequestDTO commentRequestDTO) {

        if (commentRequestDTO.getCommentContent().length() > 300) {
            throw new ApiException(ErrorCode.CONTENT_TOO_LONG);
        }
    }

    private void validation(CommentUdateRequestDTO commentUdateRequestDTO) {

        if (commentUdateRequestDTO.getCommentContent().length() > 300) {
            throw new ApiException(ErrorCode.CONTENT_TOO_LONG);
        }
    }

}
