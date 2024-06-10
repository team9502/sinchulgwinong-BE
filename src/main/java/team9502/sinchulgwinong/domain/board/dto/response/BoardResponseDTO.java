package team9502.sinchulgwinong.domain.board.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.board.entity.Board;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardResponseDTO {

    private Long userId;

    private Long boardId;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public BoardResponseDTO(Board board) {
        this.userId = board.getUser().getUserId();
        this.boardId = board.getBoardId();
        this.title = board.getBoardTitle();
        this.content = board.getBoardContent();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }
}
