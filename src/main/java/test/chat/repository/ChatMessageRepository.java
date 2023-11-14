package test.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.chat.entity.ChatMessage;
import test.chat.entity.ChatRoom;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChatRoomOrderBySendTimeDesc(ChatRoom chatRoom);
}
