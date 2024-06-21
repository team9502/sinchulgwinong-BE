package team9502.sinchulgwinong.domain.board.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardListResponseDTO {

    private Long totalBoardCount;

    private int currentPage;

    private int totalPages;

    private int pageSize;

    private List<BoardResponseDTO> boards;

    public BoardListResponseDTO(List<BoardResponseDTO> boards, Long totalBoardCount, int currentPage, int totalPages, int pageSize) {
        this.boards = boards;
        this.totalBoardCount = totalBoardCount;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
    }
}
