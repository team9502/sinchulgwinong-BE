package team9502.sinchulgwinong.domain.chat.dto.response;

import team9502.sinchulgwinong.domain.chat.entity.ChatMessage;

public class ChatMessageResponseDTO {

    private Long chatMessageId;

    private Long chatRoomId;

    private Long cpUserId;

    private Long userId;

    private String message;

    public ChatMessageResponseDTO(ChatMessage chatMessage) {
        this.chatMessageId = chatMessage.getChatMessageId();
        this.chatRoomId = chatMessage.getChatRoom().getChatRoomId();
        this.message = chatMessage.getMessage();

        if(chatMessage.getCompanyUser() != null){
            this.cpUserId = chatMessage.getCompanyUser().getCpUserId();
        }
        else{
            this.cpUserId = null;
        }

        if (chatMessage.getUser() != null){
            this.userId = chatMessage.getUser().getUserId();
        }
        else{
            this.userId = null;
        }
    }
}
