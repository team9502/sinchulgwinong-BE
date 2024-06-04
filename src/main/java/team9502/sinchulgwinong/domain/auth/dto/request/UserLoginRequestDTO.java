package team9502.sinchulgwinong.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginRequestDTO {

    @NotBlank(message = "필수 입력입니다.")
    private String email;

    @NotBlank(message = "필수 입력입니다.")
    private String password;

    @Builder
    public UserLoginRequestDTO(String email, String password) {

        this.email = email;
        this.password = password;
    }
}
