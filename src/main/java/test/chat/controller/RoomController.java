package test.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import test.chat.dto.ChatRoomDTO;
import test.chat.service.ChatRoomService;
import test.chat.service.MemberService;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chatting")
public class RoomController {

    private final MemberService memberService;
    private final ChatRoomService roomService;
    // 채팅방 주소 가져오기
    @PostMapping
    public ResponseEntity getOrCreateRoom(@RequestBody ChatRoomDTO.Post postRoomDto) {

        long roomId = roomService.createRoom(postRoomDto.getSender().getId(), postRoomDto.getReceiver().getId());

        URI location = UriComponentsBuilder.newInstance()
                .path("/chatting/{room-id}")
                .buildAndExpand(roomId)
                .toUri();

        log.info("location = {}", location);
        return ResponseEntity.created(location).build();
    }

    //  채팅방 열기
    @GetMapping("/{room-id}")
    public ResponseEntity getChatRoom(@PathVariable("room-id") long roomId) {
        ChatRoomDTO.RoomResponse room = roomService.findRoom(roomId);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

}
