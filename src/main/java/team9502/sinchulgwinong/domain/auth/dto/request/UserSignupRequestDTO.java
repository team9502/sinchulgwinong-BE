package team9502.sinchulgwinong.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.user.enums.LoginType;

@Getter
@NoArgsConstructor
@Data
public class UserSignupRequestDTO {

    @NotBlank(message = "필수 입력입니다.")
    private String username;

    @NotBlank(message = "필수 입력입니다.")
    private String nickname;

    @NotBlank(message = "필수 입력입니다.")
    private String password;

    @NotBlank(message = "필수 입력입니다.")
    private String email;

    private String phoneNumber;

    private String companyNum;

    private LoginType loginType;
}
