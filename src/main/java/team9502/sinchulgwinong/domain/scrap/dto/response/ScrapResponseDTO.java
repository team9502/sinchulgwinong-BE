package team9502.sinchulgwinong.domain.scrap.dto.response;

import lombok.Getter;
import team9502.sinchulgwinong.domain.board.entity.Board;
import team9502.sinchulgwinong.domain.scrap.entity.Scrap;

import java.time.LocalDateTime;

@Getter
public class ScrapResponseDTO {

    private Long userId;

    private Long scrapId;
    
    private Long boardId;

    private String boardTitle;

    private String userName;

    private LocalDateTime createdAt;

    public ScrapResponseDTO(Scrap scrap, Board board) {
        this.scrapId = scrap.getScrapId();
        this.boardId = scrap.getBoard().getBoardId();
        this.userId = scrap.getUser().getUserId();
        this.boardTitle = board.getBoardTitle();
        this.userName = board.getUser().getUsername();
        this.createdAt = board.getCreatedAt();
    }
}
