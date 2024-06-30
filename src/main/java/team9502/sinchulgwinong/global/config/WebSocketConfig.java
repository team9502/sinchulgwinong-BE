package team9502.sinchulgwinong.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket // 웹 소켓을 사용하도록 정의
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(webSocketHandler, "/ws/chat") // 직접 구현한 웹소켓 핸들러 (webSocketHandler)를 웹소켓이 연결될 때, Handshake할 주소 (/ws/chat)
                .addInterceptors(new HttpSessionHandshakeInterceptor()) // 세션을 웹소켓 핸들러에 전달하는 인터셉터 추가
                .setAllowedOriginPatterns("*"); // 클라이언트에서 웹 소켓 서버에 요청하는 모든 요청을 수락, CORS 방지
        // 웹소켓을 지원하지 않는 브라우저 환경에서도 비슷한 경험을 할 수 있는 기능을 제공
        // 실제 서비스에서는 "*"으로 하면 안된다. 스프링에서 웹소켓을 사용할 때, same-origin만 허용하는 것이 기본정책이다.
    }
}