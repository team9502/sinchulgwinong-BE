package team9502.sinchulgwinong.domain.jobBoard.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobStatus;
import team9502.sinchulgwinong.domain.jobBoard.entity.SalaryType;

import java.time.LocalDate;

@Getter
public class JobBoardUpdateRequestDTO {

    private String jobTitle;

    private String jobContent;

    private String regionName;

    private String subRegionName;

    private String localityName;

    @FutureOrPresent
    private LocalDate jobEndDate;

    private Integer salaryAmount;

    private String sex;

    private String address;

    private JobStatus jobStatus;

    private SalaryType salaryType;
}