package team9502.sinchulgwinong.domain.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatRequestDTO {

    private Long userId;

    private Long cpUserId;

    private Long chatRoomId;

    private String content;
}
