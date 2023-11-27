package test.chat.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import test.chat.entity.PublishMessage;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketTestController {

    @Resource(name = "chatRedisTemplate")
    private final RedisTemplate redisTemplate;
    private final ChannelTopic topic;


    @MessageMapping("/hello")
    //@SendTo("/topic/chat")
    public void message(String message) throws InterruptedException {
        //Thread.sleep(1000);
        PublishMessage publishMessage = new PublishMessage(null, null, message, LocalDateTime.now());
        // 채팅방에 메세지 전송
        redisTemplate.convertAndSend(topic.getTopic(), publishMessage);
        log.info("레디스 서버에 메세지 전송 완료");
    }
}
