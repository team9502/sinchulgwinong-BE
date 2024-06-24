package team9502.sinchulgwinong.domain.jobBoard.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class JobOpenApiResponseVO {

    @JsonProperty("row")
    private ArrayList<JobOpenApiDetailResponseDTO> jobOpenApiDetailResponseDTOS;
}
