package test.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.chat.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
}
