package team9502.sinchulgwinong.domain.chat.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomNotification {
    private Long chatRoomId;
    private String message;
}
