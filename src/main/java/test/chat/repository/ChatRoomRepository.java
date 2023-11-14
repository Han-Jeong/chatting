package test.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.chat.entity.ChatRoom;
import test.chat.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findChatRoomBySenderAndReceiver(Member sender, Member Receiver);

    List<ChatRoom> findAllBySenderOrReceiver(Member sender, Member receiver);
}
