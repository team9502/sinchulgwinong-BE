package team9502.sinchulgwinong.domain.comment.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class CommentListResponseDTO {

    private Long totalCommentCount;

    private List<CommentResponseDTO> Comment;

    public CommentListResponseDTO(List<CommentResponseDTO> commentResponseDTOS, Long totalCommentCount) {

        this.Comment = commentResponseDTOS;
        this.totalCommentCount = totalCommentCount;
    }
}
