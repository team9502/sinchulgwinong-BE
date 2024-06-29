package team9502.sinchulgwinong.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChatRequestDTO {

    @NotBlank
    private String message;
}
