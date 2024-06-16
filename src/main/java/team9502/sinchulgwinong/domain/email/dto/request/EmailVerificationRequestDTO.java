package team9502.sinchulgwinong.domain.email.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team9502.sinchulgwinong.domain.email.enums.UserType;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequestDTO {

    @Schema(description = "이메일 주소", example = "example@email.com")
    private String email;

    @Schema(description = "사용자 유형", example = "USER")
    private UserType userType;
}
