package team9502.sinchulgwinong.domain.board.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.board.entity.Board;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardResponseDTO {

    private Long userId;

    private String nickName;

    private Long boardId;

    private String title;

    private String content;

    private int commentCount;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public BoardResponseDTO(Board board, int commentCount) {
        this.userId = board.getUser().getUserId();
        this.nickName = board.getUser().getNickname();
        this.boardId = board.getBoardId();
        this.title = board.getBoardTitle();
        this.content = board.getBoardContent();
        this.commentCount = commentCount;
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }
}
