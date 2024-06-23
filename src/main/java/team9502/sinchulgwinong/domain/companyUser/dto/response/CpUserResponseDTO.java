package team9502.sinchulgwinong.domain.companyUser.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CpUserResponseDTO {

    private Long cpUserId;

    private String cpName;

    private Integer reviewCount;

    private Float averageRating;
}
