package team9502.sinchulgwinong.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CompanyUserLoginRequestDTO {

    @Schema(description = "이메일", example = "example@email.com")
    @NotBlank(message = "필수 입력입니다.")
    private String cpEmail;

    @Schema(description = "비밀번호", example = "Password1!")
    @NotBlank(message = "필수 입력입니다.")
    private String cpPassword;

    @Builder
    public CompanyUserLoginRequestDTO(String cpEmail, String cpPassword) {
        this.cpEmail = cpEmail;
        this.cpPassword = cpPassword;
    }
}
