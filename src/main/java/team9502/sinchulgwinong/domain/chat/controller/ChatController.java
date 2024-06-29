package team9502.sinchulgwinong.domain.chat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.chat.dto.request.ChatRequestDTO;
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

    @MessageMapping("/chats/chat-room/{chatRoomId}")
    @SendTo("/topic/chatroom/{chatRoomId}")
    public ChatMessageResponseDTO saveAndSendMessage(
            @RequestBody @Valid ChatRequestDTO chatRequestDTO,
            @PathVariable(name = "chatRoomId") Long chatRoomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return chatService.saveAndSendMessage(userDetails, chatRequestDTO, chatRoomId);
    }

    @GetMapping("/chats/chat-room/{chatRoomId}")
    public ResponseEntity<GlobalApiResponse<List<ChatMessageResponseDTO>>> getChatMessages(
            @PathVariable(name = "chatRoomId") Long chatRoomId) {

        List<ChatMessageResponseDTO> messages = chatService.getChatMessages(chatRoomId);

        return ResponseEntity.status(SUCCESS_READ_CHAT_MESSAGES.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_CHAT_MESSAGES.getMessage(),
                                messages)
                );
    }
}