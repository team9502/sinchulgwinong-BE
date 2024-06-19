package team9502.sinchulgwinong.domain.jobBoard.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import team9502.sinchulgwinong.domain.jobBoard.entity.SalaryType;

import java.time.LocalDate;

@Getter
public class JobBoardRequestDTO {

    @NotEmpty
    private String jobTitle;

    @NotEmpty
    private String jobContent;

    @NotEmpty
    @FutureOrPresent
    private LocalDate jobStartDate;

    @NotEmpty
    @FutureOrPresent
    private LocalDate jobEndDate;

    @NotEmpty
    private Integer salaryAmount;

    @NotEmpty
    private String sex;

    @NotEmpty
    private String address;

    @NotEmpty
    private SalaryType salaryType;
}
