package test.chat.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import test.chat.dto.ChatMessageDTO;
import test.chat.dto.MemberDTO;
import test.chat.entity.PublishMessage;
import test.chat.service.ChatMessageService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final ChatMessageService chatService;
    private final ChannelTopic topic;

    @Resource(name = "chatRedisTemplate")
    private final RedisTemplate redisTemplate;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO messageDto) {

        PublishMessage publishMessage =
                new PublishMessage(messageDto.getRoomId(),
                        messageDto.getSenderId(),
                        messageDto.getMessage(),
                        LocalDateTime.now());

        // 채팅방에 메세지 전송
        redisTemplate.convertAndSend(topic.getTopic(), publishMessage);
        log.info("레디스 서버에 메세지 전송 완료");

        chatService.saveMessage(messageDto, messageDto.getRoomId());
    }


    // 채팅메세지 가져오기
    @GetMapping("/chats/messages/{room-id}")
    public ResponseEntity getMessages(@PathVariable("room-id") long roomId,
                                      MemberDTO memberDto) {

        if(memberDto == null) {
            log.error("인증되지 않은 회원의 접근으로 메세지를 가져올 수 없음");
            throw new RuntimeException();
        }

        // 해당 채팅방의 메세지를 가져와야 함
        List<ChatMessageDTO> messages = chatService.findMessages(roomId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
