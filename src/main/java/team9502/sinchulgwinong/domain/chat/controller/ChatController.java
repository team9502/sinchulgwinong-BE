package team9502.sinchulgwinong.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team9502.sinchulgwinong.domain.chat.dto.response.ChatMessageResponseDTO;
import team9502.sinchulgwinong.domain.chat.dto.response.ChatRoomResponseDTO;
import team9502.sinchulgwinong.domain.chat.service.ChatService;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import java.util.List;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chats/cp-user/{cpUserId}")
    public ResponseEntity<GlobalApiResponse<ChatRoomResponseDTO>> createChatRoom(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("cpUserId") Long cpUserId) {

        User user = (User) userDetails.getUser();

        ChatRoomResponseDTO chatRoomResponseDTO = chatService.createChatRoom(user, cpUserId);

        return ResponseEntity.status(SUCCESS_CREATE_CHAT_ROOM.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CREATE_CHAT_ROOM.getMessage(),
                                chatRoomResponseDTO
                        )
                );
    }

    @GetMapping("/chats/chat-rooms")
    public ResponseEntity<GlobalApiResponse<List<ChatRoomResponseDTO>>> getChatRooms(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<ChatRoomResponseDTO> chatRooms = chatService.getChatRooms(userDetails);

        return ResponseEntity.status(SUCCESS_READ_CHAT_ROOM.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_CHAT_ROOM.getMessage(),
                                chatRooms)
                );
    }

    @GetMapping("/chats/chat-room/{chatRoomId}")
    public ResponseEntity<GlobalApiResponse<List<ChatMessageResponseDTO>>> getChatMessages(
            @PathVariable(name = "chatRoomId") Long chatRoomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<ChatMessageResponseDTO> messages = chatService.getChatMessages(chatRoomId, userDetails);

        return ResponseEntity.status(SUCCESS_READ_CHAT_MESSAGES.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_CHAT_MESSAGES.getMessage(),
                                messages)
                );
    }
}