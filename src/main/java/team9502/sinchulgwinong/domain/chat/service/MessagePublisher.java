package team9502.sinchulgwinong.domain.chat.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import team9502.sinchulgwinong.domain.chat.entity.ChatMessage;

@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    public void publish(ChatMessage message) {

        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
