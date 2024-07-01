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

    private boolean userRead;

    private boolean companyUserRead;

    private String lastContent;

    public ChatRoomResponseDTO(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getChatRoomId();
        this.userId = chatRoom.getUser().getUserId();
        this.cpUserId = chatRoom.getCompanyUser().getCpUserId();
        this.userName = chatRoom.getUser().getUsername();
        this.cpName = chatRoom.getCompanyUser().getCpName();
        this.chatName = chatRoom.getChatName();
        this.userRead = chatRoom.isUserRead();
        this.companyUserRead = chatRoom.isCompanyUserRead();
    }

    public ChatRoomResponseDTO(ChatRoom chatRoom, String lastContent) {
        this.chatRoomId = chatRoom.getChatRoomId();
        this.userId = chatRoom.getUser().getUserId();
        this.cpUserId = chatRoom.getCompanyUser().getCpUserId();
        this.userName = chatRoom.getUser().getUsername();
        this.cpName = chatRoom.getCompanyUser().getCpName();
        this.chatName = chatRoom.getChatName();
        this.userRead = chatRoom.isUserRead();
        this.companyUserRead = chatRoom.isCompanyUserRead();
        this.lastContent = lastContent;
    }
}
