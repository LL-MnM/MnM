package com.example.MnM.boundedContext.chat.event;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;


@RequiredArgsConstructor
@Component
public class ChatEventListener {

    private final SimpMessagingTemplate messagingTemplate;

//    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = accessor.getUser().getName();

        // 처음 접속한 사용자에게 입장 메시지를 보냅니다.
        messagingTemplate.convertAndSend("/pub/room", username + "님이 입장했습니다.");
    }
}
