package team9502.sinchulgwinong.domain.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team9502.sinchulgwinong.domain.board.entity.Board;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Getter
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private Board board;

    @Setter
    private String commentContent;
}
