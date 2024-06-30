package team9502.sinchulgwinong.domain.chat.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.chat.dto.response.ChatMessageResponseDTO;
import team9502.sinchulgwinong.domain.chat.dto.response.ChatRoomResponseDTO;
import team9502.sinchulgwinong.domain.chat.entity.ChatMessage;
import team9502.sinchulgwinong.domain.chat.entity.ChatRoom;
import team9502.sinchulgwinong.domain.chat.repository.ChatMessageRepository;
import team9502.sinchulgwinong.domain.chat.repository.ChatRoomRepository;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final CompanyUserRepository companyUserRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    //채팅방 생성
    @Transactional
    public ChatRoomResponseDTO createChatRoom(User user, Long cpUserId) {

        CompanyUser companyUser = companyUserRepository.findById(cpUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatName(companyUser.getCpName());
        chatRoom.setUser(user);
        chatRoom.setCompanyUser(companyUser);
        chatRoom.setUserRead(false);
        chatRoom.setCompanyUserRead(false);

        chatRoomRepository.save(chatRoom);

        return new ChatRoomResponseDTO(chatRoom);
    }

    // 채팅방 목록
    @Transactional
    public List<ChatRoomResponseDTO> getChatRooms(UserDetailsImpl userDetails) {

        List<ChatRoom> chatRooms = null;

        switch (userDetails.getUserType()) {
            case "USER":
                if (userDetails.getUser() != null) {
                    chatRooms = chatRoomRepository.findByUser_UserId(userDetails.getUserId());
                }
                break;
            case "COMPANY_USER":
                if (userDetails.getCpUserId() != null) {
                    chatRooms = chatRoomRepository.findByCompanyUser_CpUserId(userDetails.getCpUserId());
                }
                break;
            default:
                throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }

        if (chatRooms == null) {
            throw new ApiException(ErrorCode.CHAT_NOT_FOUND);
        }

        return chatRooms.stream()
                .map(ChatRoomResponseDTO::new)
                .collect(Collectors.toList());
    }

    //이전 채팅 내역 불러오기
    @Transactional
    public List<ChatMessageResponseDTO> getChatMessages(Long chatRoomId, UserDetailsImpl userDetails) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ApiException(ErrorCode.CHAT_NOT_FOUND));

        List<ChatMessage> messages = chatMessageRepository.findByChatRoom_ChatRoomId(chatRoomId);

        switch (userDetails.getUserType()) {
            case "USER":
                if (userDetails.getUser() != null) {
                    chatRoom.setUserRead(false);
                }
                break;
            case "COMPANY_USER":
                if (userDetails.getCpUserId() != null) {
                    chatRoom.setCompanyUserRead(false);
                }
                break;
            default:
                throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }

        chatMessageRepository.saveAll(messages);

        return messages.stream()
                .map(ChatMessageResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatMessage saveMessage(Long cpUserId, Long userId, Long chatRoomId, String content) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ApiException(ErrorCode.CHAT_NOT_FOUND));

        ChatMessage chatMessage = new ChatMessage();

        if (userId != null) {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

            chatMessage.setUser(user);
            chatMessage.setCompanyUser(null);

            chatRoom.setCompanyUserRead(true); // 유저가 메시지를 보낸 경우 읽음으로 설정
            chatRoom.setUserRead(false); // 유저가 메시지를 보낸 경우 유저는 읽음으로 설정

        } else {
            CompanyUser companyUser = companyUserRepository.findById(cpUserId)
                    .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

            chatMessage.setCompanyUser(companyUser);
            chatMessage.setUser(null);

            chatRoom.setUserRead(true); // 기업 유저가 메시지를 보낸 경우 읽음으로 설정
            chatRoom.setCompanyUserRead(false); // 기업 유저가 메시지를 보낸 경우 기업 유저는 읽음으로 설정
        }

        chatMessage.setContent(content);
        chatMessage.setChatRoom(chatRoom);

        chatRoomRepository.save(chatRoom);

        return chatMessageRepository.save(chatMessage);
    }
}