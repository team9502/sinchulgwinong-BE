package team9502.sinchulgwinong.domain.companyUser.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CpUserDeleteRequestDTO {

    @Schema(description = "기업 회원 비밀번호", example = "Password1!")
    private String password;
}
