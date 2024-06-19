package team9502.sinchulgwinong.domain.jobBoard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class AdJobBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AdJobBoardId;

    @Setter
    @OneToOne
    @JoinColumn(name = "job_board_id")
    private JobBoard jobBoard;
}
