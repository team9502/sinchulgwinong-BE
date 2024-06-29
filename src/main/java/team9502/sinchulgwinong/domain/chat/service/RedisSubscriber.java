package team9502.sinchulgwinong.domain.chat.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team9502.sinchulgwinong.domain.chat.entity.ChatMessage;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatMessage chatMessage = objectMapper.readValue(message.getBody(), ChatMessage.class);
            messagingTemplate.convertAndSend("/topic/chatroom/" +
                    chatMessage.getChatRoom().getChatRoomId(), chatMessage);

            // 채팅방 목록 갱신 및 새로운 메시지 알림
            messagingTemplate.convertAndSend("/topic/chatrooms",
                    new ChatRoomNotification(chatMessage.getChatRoom().getChatRoomId(), "New message"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}