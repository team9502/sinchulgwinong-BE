package team9502.sinchulgwinong.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordUpdateRequestDTO {

    @Schema(description = "현재 비밀번호", example = "password")
    private String currentPassword;

    @Schema(description = "변경할 비밀번호", example = "newPassword")
    private String newPassword;

    @Schema(description = "변경할 비밀번호 확인", example = "newPassword")
    private String newPasswordConfirm;
}
