package team9502.sinchulgwinong.domain.companyUser.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CpUserPageResponseDTO {

    private List<CpUserResponseDTO> cpUsers;

    private int totalCpUserCount;

    private int currentPage;

    private int totalPages;
}
