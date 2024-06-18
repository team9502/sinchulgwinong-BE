package team9502.sinchulgwinong.domain.scrap.dto.response;

import lombok.Getter;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;
import team9502.sinchulgwinong.domain.scrap.entity.JobScrap;

@Getter
public class JobScrapResponseDTO {

    private Long userId;

    private Long scrapId;

    private Long jobBoardId;

    public JobScrapResponseDTO(JobScrap jobScrap, JobBoard jobBoard) {
        this.scrapId = jobScrap.getJobScrapId();
        this.jobBoardId = jobScrap.getJobBoard().getJobBoardId();
        this.userId = jobScrap.getUser().getUserId();
        //TODO: 스크랩 조회시 나올 응답값은 피그마 수정후 추가
    }
}