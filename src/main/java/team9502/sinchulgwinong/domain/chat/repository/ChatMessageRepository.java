package team9502.sinchulgwinong.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.chat.entity.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoom_ChatRoomId(Long chatRoomId);

    List<ChatMessage> findByChatRoom_ChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);
}
