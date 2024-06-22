package team9502.sinchulgwinong.domain.faq.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FaqResponseDTO {

    private Long faqId;

    private String faqTitle;

    private String faqContent;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
