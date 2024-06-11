package team9502.sinchulgwinong.domain.scrap.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team9502.sinchulgwinong.domain.board.entity.Board;
import team9502.sinchulgwinong.domain.user.entity.User;

@Getter
@Entity
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "boardId")
    private Board board;

    @Setter
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
