package team9502.sinchulgwinong.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUserLoginResponseDTO {

    private Long cpUserId;

    private String cpUsername;

    private String cpName;

    private String cpEmail;

    private String cpPhoneNumber;

    private Boolean hiringStatus;

    private Integer employeeCount;
}