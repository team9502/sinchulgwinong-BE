package team9502.sinchulgwinong.domain.jobBoard.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class JobOpenApiDetailResponseDTO {

    @JsonProperty("COMPANY_NAME")
    private String companyName;

    @JsonProperty("EMP_TITLE")
    private String jobTitle;

    @JsonProperty("EMP_PAY")
    private String salaryAmount;

    @JsonProperty("EXPECTED_WORK_ADRESS")
    private String address;

    @JsonProperty("CREATE_DATE")
    private String createDate;

    @JsonProperty("CLOSE_DATE")
    private String closeDate;

}
