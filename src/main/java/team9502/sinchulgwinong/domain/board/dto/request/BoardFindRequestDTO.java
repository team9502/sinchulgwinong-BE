package team9502.sinchulgwinong.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardFindRequestDTO {

    @NotBlank
    private String findBoardTitle;
}
