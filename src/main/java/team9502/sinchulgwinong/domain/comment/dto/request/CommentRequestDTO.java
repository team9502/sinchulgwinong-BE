package team9502.sinchulgwinong.domain.comment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDTO {

    @NotNull
    private String commentContent;
}

