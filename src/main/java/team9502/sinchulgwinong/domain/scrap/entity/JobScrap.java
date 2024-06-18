package team9502.sinchulgwinong.domain.scrap.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;
import team9502.sinchulgwinong.domain.user.entity.User;

@Getter
@Entity
public class JobScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobScrapId;

    @Setter
    @OneToOne
    @JoinColumn(name = "job_board_id")
    private JobBoard jobBoard;

    @Setter
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
