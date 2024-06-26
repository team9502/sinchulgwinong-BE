package team9502.sinchulgwinong.domain.jobBoard.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import team9502.sinchulgwinong.domain.jobBoard.entity.SalaryType;

import java.time.LocalDate;

@Getter
public class JobBoardRequestDTO {

    @NotBlank
    private String jobTitle;

    @NotBlank
    private String jobContent;

    @NotBlank
    private String regionName;

    @NotBlank
    private String subRegionName;

    @NotBlank
    private String localityName;

    @NotBlank
    private String majorCategoryName;

    @NotBlank
    private String minorCategoryName;

    @FutureOrPresent
    private LocalDate jobEndDate;

    @NotNull
    private Integer salaryAmount;

    @NotBlank
    private String sex;

    @NotBlank
    private String address;

    @NotNull
    private SalaryType salaryType;
}
