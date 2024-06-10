package team9502.sinchulgwinong.domain.comment.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.board.entity.Board;
import team9502.sinchulgwinong.domain.board.repository.BoardRepository;
import team9502.sinchulgwinong.domain.comment.dto.request.CommentRequestDTO;
import team9502.sinchulgwinong.domain.comment.dto.response.CommentResponseDTO;
import team9502.sinchulgwinong.domain.comment.entity.Comment;
import team9502.sinchulgwinong.domain.comment.repository.CommentRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CommentResponseDTO createComment(Long boardId, User user, CommentRequestDTO commentRequestDTO) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        Comment comment = new Comment();

        comment.setUser(user);
        comment.setBoard(board);
        comment.setCommentContent(commentRequestDTO.getCommentContent());

        commentRepository.save(comment);

        return new CommentResponseDTO(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getAllComment(Long boardId) {

        commentRepository.findByBoard_BoardId(boardId);

        return commentRepository.findByBoard_BoardId(boardId).stream()
                .map(CommentResponseDTO::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public CommentResponseDTO updateComment(Long boardId, Long commentId, User user, CommentRequestDTO commentRequestDTO) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMMENT_NOT_FOUND));

        if (!user.getUserId().equals(comment.getUser().getUserId())) {
            throw new ApiException(ErrorCode.FORBIDDEN_WORK);
        }

        if (!comment.getBoard().getBoardId().equals(boardId)) {
            throw new ApiException(ErrorCode.BOARD_NOT_FOUND);
        }

        comment.setCommentContent(commentRequestDTO.getCommentContent());

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

}
