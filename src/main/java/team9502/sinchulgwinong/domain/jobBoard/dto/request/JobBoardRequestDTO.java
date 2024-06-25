package team9502.sinchulgwinong.domain.jobBoard.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import team9502.sinchulgwinong.domain.jobBoard.entity.SalaryType;

import java.time.LocalDate;

@Getter
public class JobBoardRequestDTO {

    @NotNull
    private String jobTitle;

    @NotNull
    private String jobContent;

    @NotNull
    private String regionName;

    @NotNull
    private String subRegionName;

    @NotNull
    private String localityName;

    @FutureOrPresent
    private LocalDate jobEndDate;

    @NotNull
    private Integer salaryAmount;

    @NotNull
    private String sex;

    @NotNull
    private String address;

    @NotNull
    private SalaryType salaryType;
}
