package team9502.sinchulgwinong.domain.board.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class BoardRequestDTO {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;
}
