package test.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import test.chat.entity.PublishMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    public void sendMessage(String message) {
        try {
            log.info("publish 전 message: {}", message);
            PublishMessage publishMessage = objectMapper.readValue(message, PublishMessage.class);
            messagingTemplate.convertAndSend("/sub/chats/" + publishMessage.getRoomId(), publishMessage);
            log.info("publish 전 message: {}", publishMessage.getContent());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
