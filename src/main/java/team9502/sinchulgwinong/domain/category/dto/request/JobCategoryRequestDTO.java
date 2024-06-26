package team9502.sinchulgwinong.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class JobCategoryRequestDTO {

    @NotBlank
    private String majorCategoryName;

    @NotBlank
    private String minorCategoryName;
}
