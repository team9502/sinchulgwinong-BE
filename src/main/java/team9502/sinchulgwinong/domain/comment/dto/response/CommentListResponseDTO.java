package team9502.sinchulgwinong.domain.comment.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class CommentListResponseDTO {

    private Long totalCommentCount;

    private int currentPage;

    private int totalPages;

    private int pageSize;

    private List<CommentResponseDTO> Comment;

    public CommentListResponseDTO(List<CommentResponseDTO> comment, Long totalCommentCount, int currentPage, int totalPages, int pageSize) {
        this.totalCommentCount = totalCommentCount;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.Comment = comment;
    }
}
