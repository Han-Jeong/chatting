package test.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.chat.dto.ChatRoomDTO;
import test.chat.dto.MemberDto;
import test.chat.entity.ChatRoom;
import test.chat.entity.Member;
import test.chat.repository.ChatRoomRepository;
import test.chat.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository roomRepository;

    @Transactional
    public Long createRoom(long receiverId, long senderId) {
        log.info("receiverId ={}",receiverId);
        log.info("senderId ={}",senderId);
        Optional<Member> receiverById = memberRepository.findById(receiverId);
        Optional<Member> senderById = memberRepository.findById(senderId);


        // 둘의 채팅이 있는 지 확인
        Optional<ChatRoom> optionalChatRoom = roomRepository
                .findChatRoomBySenderAndReceiver(senderById.get(), receiverById.get());
        Optional<ChatRoom> optionalChatRoom2 = roomRepository
                .findChatRoomBySenderAndReceiver(receiverById.get(), senderById.get());

        ChatRoom chatRoom = null;

        if(optionalChatRoom.isPresent()) {
            chatRoom = optionalChatRoom.get();
            log.info("find chat room");
            return chatRoom.getId();
        } else if (optionalChatRoom2.isPresent()) {
            chatRoom = optionalChatRoom2.get();
            log.info("find chat room");
            return chatRoom.getId();
        } else {
            chatRoom = ChatRoomDTO.Post.toEntity(senderById.get(), receiverById.get());
            log.info("create chat room");
        }

        ChatRoom saveChatRoom = roomRepository.save(chatRoom);
        log.info("saeChatRoomId =", saveChatRoom.getId());

        return saveChatRoom.getId();
    }

    // 유저의 채팅 목록 가져오기
    public List<ChatRoom> findRooms(MemberDto memberDto) {
        Member member = memberRepository.findByUsername(memberDto.getUsername());
        List<ChatRoom> chatRooms = roomRepository.findAllBySenderOrReceiver(member,member);
        return chatRooms;
    }

    // 채팅방 하나 찾기
    @Transactional(readOnly = true)
    public ChatRoomDTO.RoomResponse findRoom(long roomId) {
        ChatRoom chatRoom = findExistRoom(roomId);
        ChatRoomDTO.RoomResponse dto = ChatRoomDTO.RoomResponse.toDto(chatRoom);
        return dto;
    }

    // 채팅방 존재 검증
    private ChatRoom findExistRoom(long roomId) {
        Optional<ChatRoom> optionalChatRoom = roomRepository.findById(roomId);
        if (optionalChatRoom.isPresent()) {
            return optionalChatRoom.get();
        }
        return null;
    }
}
