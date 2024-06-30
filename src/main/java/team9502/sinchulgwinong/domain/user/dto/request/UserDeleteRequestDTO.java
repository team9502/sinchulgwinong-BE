package team9502.sinchulgwinong.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDeleteRequestDTO {

    @Schema(description = "비밀번호", example = "Password1!")
    private String password;
}
