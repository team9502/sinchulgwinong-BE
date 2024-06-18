package team9502.sinchulgwinong.domain.scrap.dto.response;

import lombok.Getter;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;
import team9502.sinchulgwinong.domain.scrap.entity.JobScrap;

import java.time.LocalDateTime;

@Getter
public class JobScrapResponseDTO {

    private Long userId;

    private Long scrapId;

    private Long jobBoardId;

    private String boardTitle;

    private String userName;

    private LocalDateTime createdAt;

    public JobScrapResponseDTO(JobScrap jobScrap, JobBoard jobBoard) {
        this.scrapId = jobScrap.getJobScrapId();
        this.jobBoardId = jobScrap.getJobBoard().getJobBoardId();
        this.userId = jobScrap.getUser().getUserId();

    }
}