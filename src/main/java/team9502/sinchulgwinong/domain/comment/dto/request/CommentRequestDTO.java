package team9502.sinchulgwinong.domain.comment.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CommentRequestDTO {

    @NotEmpty
    private String commentContent;
}

