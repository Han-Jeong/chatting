package test.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.chat.dto.ChatMessageDTO;
import test.chat.entity.ChatMessage;
import test.chat.entity.ChatRoom;
import test.chat.entity.Member;
import test.chat.repository.ChatMessageRepository;
import test.chat.repository.ChatRoomRepository;
import test.chat.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository messageRepository;

    @Transactional
    public void saveMessage(ChatMessageDTO dto, Long roomId) {
        Member member = memberRepository.findById(dto.getSenderId()).get();
        Optional<ChatRoom> byId = roomRepository.findById(roomId);

        ChatMessage chatMessage = ChatMessage.builder()
                .content(dto.getMessage())
                .sender(member)
                .chatRoom(byId.get())
                .sendTime(LocalDateTime.now())
                .build();
        messageRepository.save(chatMessage);
        log.info("save message completely");
    }

    public List<ChatMessageDTO> findMessages(long roomId) {
        Optional<ChatRoom> byId = roomRepository.findById(roomId);
        List<ChatMessage> chatMessage = messageRepository.findAllByChatRoomOrderBySendTimeDesc(byId.get());

        return chatMessage.stream().map(ChatMessageDTO::toDto).collect(Collectors.toList());
    }
}
