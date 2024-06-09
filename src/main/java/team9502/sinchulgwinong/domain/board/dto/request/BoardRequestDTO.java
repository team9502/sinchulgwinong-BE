package team9502.sinchulgwinong.domain.board.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class BoardRequestDTO {

    @NotEmpty
    String title;

    @NotEmpty
    String content;
}
