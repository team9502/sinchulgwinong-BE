package team9502.sinchulgwinong.domain.jobBoard.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class JobBoardListResponseDTO {

    private Long totalJobBoardCount;

    private List<JobBoardResponseDTO> jobBoardResponseDTOS;

    public JobBoardListResponseDTO(List<JobBoardResponseDTO> jobBoardResponseDTOS, Long totalJobBoardCount) {
        this.jobBoardResponseDTOS = jobBoardResponseDTOS;
        this.totalJobBoardCount = totalJobBoardCount;
    }
}
