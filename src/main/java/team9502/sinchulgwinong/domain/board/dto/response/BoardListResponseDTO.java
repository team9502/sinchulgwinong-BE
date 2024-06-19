package team9502.sinchulgwinong.domain.board.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardListResponseDTO {

    private Long totalBoardCount;

    private List<BoardResponseDTO> boards;

    public BoardListResponseDTO(Long totalBoardCount, List<BoardResponseDTO> boards) {
        this.totalBoardCount = totalBoardCount;
        this.boards = boards;
    }
}
