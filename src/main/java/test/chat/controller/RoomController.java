package test.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test.chat.dto.ChatRoomDTO;
import test.chat.service.ChatRoomService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class RoomController {

    private final ChatRoomService roomService;

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }
    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoomDTO.RoomResponse> room() {
        return roomService.findAllRoom();
    }
    // 채팅방 주소 가져오기
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity createRoom(@RequestBody ChatRoomDTO.Post postRoomDto, @RequestParam String roomName) {
        long roomId = roomService.createRoom(
                postRoomDto.getSender().getId(),
                postRoomDto.getReceiver().getId(),
                roomName);
        return new ResponseEntity<>(roomId, HttpStatus.OK);
    }

    //  채팅방 열기
    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable Long roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }
    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoomDTO.RoomResponse roomInfo(@PathVariable Long roomId) {
        return roomService.findRoom(roomId);
    }

}
