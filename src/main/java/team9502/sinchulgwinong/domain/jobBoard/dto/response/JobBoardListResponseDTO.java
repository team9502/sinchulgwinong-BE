package team9502.sinchulgwinong.domain.jobBoard.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class JobBoardListResponseDTO {

    private Long totalJobBoardCount;

    private int currentPage;

    private int totalPages;

    private int pageSize;

    private List<JobBoardResponseDTO> jobBoardResponseDTOS;

    public JobBoardListResponseDTO(List<JobBoardResponseDTO> jobBoardResponseDTOS, Long totalJobBoardCount, int currentPage, int totalPages, int pageSize) {
        this.jobBoardResponseDTOS = jobBoardResponseDTOS;
        this.totalJobBoardCount = totalJobBoardCount;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
    }
}
