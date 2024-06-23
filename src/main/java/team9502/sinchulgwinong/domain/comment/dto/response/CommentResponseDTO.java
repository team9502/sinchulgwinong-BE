package team9502.sinchulgwinong.domain.comment.dto.response;

import lombok.Getter;
import team9502.sinchulgwinong.domain.comment.entity.Comment;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDTO {

    private Long commentId;

    private Long userId;

    private String nickName;

    private Long boardId;

    private String commentContent;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public CommentResponseDTO(Comment comment) {
        this.commentId = comment.getCommentId();
        this.userId = comment.getUser().getUserId();
        this.nickName = comment.getUser().getNickname();
        this.boardId = comment.getBoard().getBoardId();
        this.commentContent = comment.getCommentContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
