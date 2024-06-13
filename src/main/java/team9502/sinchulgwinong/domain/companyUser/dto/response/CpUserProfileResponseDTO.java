package team9502.sinchulgwinong.domain.companyUser.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CpUserProfileResponseDTO {

    @Schema(description = "기업(회원) ID", example = "1")
    private Long cpUserId;

    @Schema(description = "회사명", example = "팀9502")
    private String cpName;

    @Schema(description = "기업 이메일", example = "example@email.com")
    private String cpEmail;

    @Schema(description = "기업 전화번호", example = "01012345678")
    private String cpPhoneNumber;

    @Schema(description = "대표자", example = "김은채")
    private String cpUsername;

    @Schema(description = "고용 여부", example = "true")
    private Boolean hiringStatus;

    @Schema(description = "직원 수", example = "10")
    private Integer employeeCount;

    @Schema(description = "설립일", example = "2021-01-01")
    private LocalDate foundationDate;

    @Schema(description = "기업 설명", example = "팀9502의 백엔드 개발자들은 소중한 존재입니다. 우린 개발자 친화적인 기업입니다.")
    private String description;
}
