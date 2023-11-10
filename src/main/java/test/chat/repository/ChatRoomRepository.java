package test.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.chat.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
