package test.chat.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import test.chat.dto.ChatDTO;
import test.chat.entity.ChatRoom;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;

    private final ChatService service;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

//        TextMessage textMessage = new TextMessage("Welcome Chatting Server");
//        session.sendMessage(textMessage);

        ChatDTO chatMessage = mapper.readValue(payload, ChatDTO.class);
        log.info("session {}", chatMessage.toString());

        ChatRoomDTO room = service.findRoomById(chatMessage.getRoomId());
        log.info("room {}", room.toString());

        room.handleAction(session, chatMessage, service);
    }
}
