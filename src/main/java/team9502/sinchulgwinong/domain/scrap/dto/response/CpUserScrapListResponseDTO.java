package team9502.sinchulgwinong.domain.scrap.dto.response;

import lombok.Getter;
import team9502.sinchulgwinong.domain.companyUser.dto.response.CpUserProfileResponseDTO;

import java.util.List;

@Getter
public class CpUserScrapListResponseDTO {

    private Long totalCpScrapCount;

    private int currentPage;

    private int totalPages;

    private int pageSize;

    private List<CpUserProfileResponseDTO> cpUserProfileResponseDTOS;

    public CpUserScrapListResponseDTO(
            List<CpUserProfileResponseDTO> cpUserProfileResponseDTOS,
            Long totalCpScrapCount,
            int currentPage,
            int totalPages,
            int pageSize) {

        this.totalCpScrapCount = totalCpScrapCount;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.cpUserProfileResponseDTOS = cpUserProfileResponseDTOS;
    }
}
