package team9502.sinchulgwinong.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyUserLoginRequestDTO {

    @NotBlank(message = "필수 입력입니다.")
    private String cpEmail;

    @NotBlank(message = "필수 입력입니다.")
    private String cpPassword;

    @Builder
    public CompanyUserLoginRequestDTO(String cpEmail, String cpPassword) {
        this.cpEmail = cpEmail;
        this.cpPassword = cpPassword;
    }
}
