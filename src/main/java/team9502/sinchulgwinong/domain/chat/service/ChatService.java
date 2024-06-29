package team9502.sinchulgwinong.domain.chat.service;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.chat.dto.request.ChatRequestDTO;
import team9502.sinchulgwinong.domain.chat.dto.response.ChatMessageResponseDTO;
import team9502.sinchulgwinong.domain.chat.dto.response.ChatRoomResponseDTO;
import team9502.sinchulgwinong.domain.chat.entity.ChatMessage;
import team9502.sinchulgwinong.domain.chat.entity.ChatRoom;
import team9502.sinchulgwinong.domain.chat.repository.ChatMessageRepository;
import team9502.sinchulgwinong.domain.chat.repository.ChatRoomRepository;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final CompanyUserRepository companyUserRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public ChatRoomResponseDTO createChatRoom(User user, Long cpUserId) {

        CompanyUser companyUser = companyUserRepository.findById(cpUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatName(companyUser.getCpName());
        chatRoom.setUser(user);
        chatRoom.setCompanyUser(companyUser);
        chatRoom.setChatCheck(false);

        chatRoomRepository.save(chatRoom);

        notifyChatRoomUpdate();

        return new ChatRoomResponseDTO(chatRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponseDTO> getChatRooms(UserDetailsImpl userDetails) {
        List<ChatRoom> chatRooms;

        if (userDetails.getUser() != null) {
            chatRooms = chatRoomRepository.findByUser_UserId(userDetails.getUserId());
        } else {
            chatRooms = chatRoomRepository.findByCompanyUser_CpUserId(userDetails.getCpUserId());
        }

        return chatRooms.stream()
                .map(ChatRoomResponseDTO::new)
                .collect(Collectors.toList());
    }

    public void notifyChatRoomUpdate() {
        List<ChatRoomResponseDTO> chatRooms = chatRoomRepository.findAll()
                .stream()
                .map(ChatRoomResponseDTO::new)
                .collect(Collectors.toList());

        messagingTemplate.convertAndSend("/topic/chatrooms", chatRooms);
    }

    @Transactional
    public ChatMessageResponseDTO saveAndSendMessage(UserDetailsImpl userDetails, ChatRequestDTO chatRequestDTO, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ApiException(ErrorCode.CHAT_NOT_FOUND));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(chatRequestDTO.getMessage());
        chatMessage.setChatRoom(chatRoom);

        if (userDetails.getUser() != null) {
            User user = (User) userDetails.getUser();
            chatMessage.setUser(user);
            chatMessage.setCompanyUser(null);
        } else if (userDetails.getCpUserId() != null) {
            CompanyUser companyUser = companyUserRepository.findById(userDetails.getCpUserId())
                    .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));
            chatMessage.setCompanyUser(companyUser);
            chatMessage.setUser(null);
        }

        chatMessageRepository.save(chatMessage);

        chatRoom.setChatCheck(true);
        chatRoomRepository.save(chatRoom);

        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoomId, new ChatMessageResponseDTO(chatMessage));

        notifyChatRoomUpdate();

        return new ChatMessageResponseDTO(chatMessage);
    }

    @Transactional
    public List<ChatMessageResponseDTO> getChatMessages(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ApiException(ErrorCode.CHAT_NOT_FOUND));

        List<ChatMessage> messages = chatMessageRepository.findByChatRoom_ChatRoomId(chatRoomId);

        if (chatRoom.isChatCheck()) {
            chatRoom.setChatCheck(false);
        }

        chatMessageRepository.saveAll(messages);

        return messages.stream()
                .map(ChatMessageResponseDTO::new)
                .collect(Collectors.toList());
    }
}