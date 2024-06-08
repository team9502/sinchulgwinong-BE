package team9502.sinchulgwinong.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CpUserSignupRequestDTO {

    @NotNull(message = "고용 여부를 입력해주세요.")
    @Schema(description = "고용 여부", example = "true")
    private Boolean hiringStatus;

    @Schema(description = "직원 수", example = "10")
    private Integer employeeCount;

    @JsonFormat(pattern = "yyyyMMdd")
    @Schema(description = "설립일", example = "19991006", format = "date")
    private LocalDate foundationDate;

    @Schema(description = "회사 설명", example = "고양이는 세상을 움직이는 혁신입니다.")
    private String description;

    @NotBlank(message = "사업자 번호를 입력해주세요.")
    @Schema(description = "사업자 번호", example = "1234567890")
    private String cpNum;

    @NotBlank(message = "회사 이름을 입력해주세요.")
    @Schema(description = "회사 이름", example = "고양이탕후루")
    private String cpName;

    @NotBlank(message = "대표자 이름을 입력해주세요.")
    @Schema(description = "대표자 이름", example = "김고양이")
    private String cpUsername;

    @NotBlank(message = "회사 이메일을 입력해주세요.")
    @Schema(description = "회사 이메일", example = "example@email.com")
    private String cpEmail;

    @NotBlank(message = "회사 전화번호를 입력해주세요.")
    @Schema(description = "회사 전화번호", example = "01012345678")
    private String cpPhoneNumber;

    @NotBlank(message = "회사 비밀번호를 입력해주세요.")
    @Schema(description = "회사 비밀번호", example = "password")
    private String cpPassword;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    @Schema(description = "비밀번호 확인", example = "password")
    private String cpConfirmPassword;

    @Schema(description = "약관 동의 여부", example = "true")
    private boolean agreeToTerms;
}
