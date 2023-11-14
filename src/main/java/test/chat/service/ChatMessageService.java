package test.chat.service;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.chat.dto.ChatMessageDTO;
import test.chat.entity.ChatMessage;
import test.chat.entity.ChatRoom;
import test.chat.entity.Member;
import test.chat.repository.ChatMessageRepository;
import test.chat.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomService roomService;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository messageRepository;

    @Resource
    private final RedisTemplate<String, ChatMessageDTO> redisTemplate;


    private static final String MESSAGE_CACHE_KEY = "messageCacheRoom:";

    private ChatMessage convertValue(ChatMessageDTO dto, Long roomId) {
        Member member = memberRepository.findByUsername(dto.getSender());
        ChatRoom chatRoom = roomService.findRoom(roomId);

        ChatMessage chatMessage = ChatMessage.builder()
                .content(dto.getMessage())
                .sender(member)
                .chatRoom(chatRoom)
                .sendTime(LocalDateTime.now())
                .build();
        return chatMessage;
    }

    public void cachedMessage(ChatMessageDTO dto, Long roomId) {
        String cacheKey = MESSAGE_CACHE_KEY+roomId;
        redisTemplate.opsForList().rightPush(cacheKey, dto);
    }

    @Scheduled(cron = "0 0 0/1 * * *") // 한시간마다
    @Transactional
    public void saveMessages() {
        // 레디스에 캐싱된 채팅방 아이디만 파싱
        List<Long> roomIdList = redisTemplate.keys(MESSAGE_CACHE_KEY+"*").stream()
                .map(key -> Long.parseLong(key.substring(MESSAGE_CACHE_KEY.length())))
                .collect(Collectors.toList());
        // 각 채팅방의 캐싱된 메세지를 찾아 DB에 저장한 후, 캐싱된 메세지는 삭제
        for(Long id : roomIdList) {
            String cacheKey = MESSAGE_CACHE_KEY + id;
            try{
                List<ChatMessageDTO> dtoList = redisTemplate.opsForList().range(cacheKey, 0, -1);
                List<ChatMessage> messages = new ArrayList<>();
                for (ChatMessageDTO dto : dtoList) {
                    ChatMessage chatMessage = convertValue(dto, dto.getRoomId());
                    messages.add(chatMessage);
                }
                if(messages != null && messages.size() > 0) {
                    messageRepository.saveAll(messages);
                    //RDB에 저장한 데이터는 캐시에서 지운다
                    redisTemplate.opsForList().trim(cacheKey, messages.size(), -1);
                } else {
                    continue;
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public List<ChatMessageDTO> findMessages(long roomId) {
        String cacheKey = MESSAGE_CACHE_KEY+roomId;

        List<ChatMessageDTO> cachedMessages = redisTemplate.opsForList().range(cacheKey, 0, -1);

        ChatRoom chatRoom = roomService.findRoom(roomId);
        List<ChatMessageDTO> allMessages = new ArrayList<>();
        // 캐시된 메세지 수가 요청한 페이지 사이즈보다 적을 경우
        List<ChatMessage> dbMessages = messageRepository.findAllByChatRoomOrderBySendTimeDesc(chatRoom);
        allMessages.addAll(dbMessages.stream().map(ChatMessageDTO::toDto).collect(Collectors.toList()));
        allMessages.addAll(cachedMessages);
        return allMessages;
    }
}
