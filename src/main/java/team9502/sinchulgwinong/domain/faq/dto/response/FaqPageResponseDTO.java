package team9502.sinchulgwinong.domain.faq.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FaqPageResponseDTO {

    private List<FaqListResponseDTO> faqs;

    private int totalFaqCount;

    private int currentPage;

    private int totalPages;
}
