package team9502.sinchulgwinong.domain.faq.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqListResponseDTO {

    private Long faqId;

    private String faqTitle;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private int viewCount;
}
