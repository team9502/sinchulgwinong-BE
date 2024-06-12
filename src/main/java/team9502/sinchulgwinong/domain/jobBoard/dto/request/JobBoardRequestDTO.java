package team9502.sinchulgwinong.domain.jobBoard.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobStatus;
import team9502.sinchulgwinong.domain.jobBoard.entity.SalaryType;

import java.time.LocalDate;

@Getter
public class JobBoardRequestDTO {

    private String jobTitle;

    private String jobContent;

    @FutureOrPresent
    private LocalDate jobStartDate;

    @FutureOrPresent
    private LocalDate jobEndDate;

    private Integer salaryAmount;

    private String sex;

    private String address;

    private JobStatus jobStatus;

    private SalaryType salaryType;
}
