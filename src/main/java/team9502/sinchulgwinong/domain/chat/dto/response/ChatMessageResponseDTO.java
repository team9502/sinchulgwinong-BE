package team9502.sinchulgwinong.domain.chat.dto.response;

import lombok.Getter;
import team9502.sinchulgwinong.domain.chat.entity.ChatMessage;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponseDTO {

    private Long chatMessageId;

    private Long chatRoomId;

    private Long cpUserId;

    private Long userId;

    private String message;

    private LocalDateTime createAt;

    public ChatMessageResponseDTO(ChatMessage chatMessage) {
        this.chatMessageId = chatMessage.getChatMessageId();
        this.chatRoomId = chatMessage.getChatRoom().getChatRoomId();
        this.message = chatMessage.getMessage();
        this.createAt = chatMessage.getCreatedAt();

        if (chatMessage.getCompanyUser() != null) {
            this.cpUserId = chatMessage.getCompanyUser().getCpUserId();
        } else {
            this.cpUserId = null;
        }

        if (chatMessage.getUser() != null) {
            this.userId = chatMessage.getUser().getUserId();
        } else {
            this.userId = null;
        }
    }
}
