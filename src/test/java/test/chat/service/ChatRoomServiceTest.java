package test.chat.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChatRoomServiceTest {

    @Autowired
    ChatRoomService roomService;
    @Autowired
    MemberService memberService;

    @Test
    void createRoom() {
        Long userA = memberService.createMember("userA");
        Long userB = memberService.createMember("userB");

        Long room = roomService.createRoom(userA, userB);

        System.out.println("room = " + room);

        Long room1 = roomService.createRoom(userA, userB);
        Long room2 = roomService.createRoom(userB, userA);
        System.out.println("room1 = " + room1);
        System.out.println("room2 = " + room2);
    }

}