package team9502.sinchulgwinong.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class JobBoardCategoryRequestDTO {

    @NotBlank
    private String regionName;

    @NotBlank
    private String subRegionName;

    @NotBlank
    private String localityName;
}
