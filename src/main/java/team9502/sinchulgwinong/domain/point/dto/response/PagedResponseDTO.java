package team9502.sinchulgwinong.domain.point.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponseDTO<T> {

    private List<T> data;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNextPage;
}
