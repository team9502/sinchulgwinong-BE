package team9502.sinchulgwinong.domain.faq.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FaqUpdateRequestDTO {

    @Size(max = 100, message = "FAQ 제목은 100자를 초과할 수 없습니다.")
    @Schema(description = "FAQ 제목 수정", example = "수정된 제목")
    private String faqTitle;

    @Size(max = 1000, message = "FAQ 내용은 1000자를 초과할 수 없습니다.")
    @Schema(description = "FAQ 내용 수정", example = "수정된 내용")
    private String faqContent;
}
