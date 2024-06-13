package team9502.sinchulgwinong.domain.companyUser.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CpUserProfileUpdateRequestDTO {

    @Schema(description = "직원수", example = "100")
    private Integer employeeCount;

    @Schema(description = "고용 여부", example = "true")
    private Boolean hiringStatus;

    @Schema(description = "소개", example = "수정된 기업 소개입니다.")
    private String description;

    @Schema(description = "기업 이메일", example = "fix@email.com")
    private String cpEmail;

    @Schema(description = "기업 전화번호", example = "01012345678")
    private String cpPhoneNumber;
}
