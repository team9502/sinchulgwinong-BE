package team9502.sinchulgwinong.domain.chat.dto.response;

import lombok.Getter;
import team9502.sinchulgwinong.domain.chat.entity.ChatRoom;

@Getter
public class ChatRoomResponseDTO {

    private Long chatRoomId;

    private Long userId;

    private Long cpUserId;

    private String userName;

    private String cpName;

    private String chatName;

    private boolean chatCheck;

    public ChatRoomResponseDTO(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getChatRoomId();
        this.userId = chatRoom.getUser().getUserId();
        this.cpUserId = chatRoom.getCompanyUser().getCpUserId();
        this.userName = chatRoom.getUser().getUsername();
        this.cpName = chatRoom.getCompanyUser().getCpName();
        this.chatName = chatRoom.getChatName();
        this.chatCheck = chatRoom.isChatCheck();
    }
}
