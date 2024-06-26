package team9502.sinchulgwinong.domain.board.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BoardRequestDTO {

    @NotNull
    private String boardTitle;

    @NotNull
    private String boardContent;
}
