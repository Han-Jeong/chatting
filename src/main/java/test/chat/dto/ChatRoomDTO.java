package test.chat.dto;

import lombok.Builder;
import lombok.Data;
import test.chat.entity.ChatRoom;
import test.chat.entity.Member;

@Data
@Builder
public class ChatRoomDTO {
    private Long roomId; // 채팅방 아이디
    private String sender;
    private String receiver;


    public static ChatRoomDTO toDto(ChatRoom room){
        return ChatRoomDTO.builder()
                .roomId(room.getId())
                .sender(room.getSender().getUsername())
                .receiver(room.getReceiver().getUsername())
                .build();
    }

    public ChatRoom toEntity(Member sender, Member receiver) {
        return ChatRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }

//    public void handleAction(WebSocketSession session, ChatMessageDTO message, ChatService service) {
//        // message 에 담긴 타입을 확인한다.
//        // 이때 message 에서 getType 으로 가져온 내용이
//        // ChatDTO 의 열거형인 MessageType 안에 있는 ENTER 과 동일한 값이라면
//        if (message.getType().equals(ChatMessageDTO.MessageType.ENTER)) {
//            // sessions 에 넘어온 session 을 담고,
//            sessions.add(session);
//
//            // message 에는 입장하였다는 메시지를 띄운다
//            message.setMessage(message.getSender() + " 님이 입장하셨습니다");
//            sendMessage(message, service);
//        } else if (message.getType().equals(ChatMessageDTO.MessageType.TALK)) {
//            message.setMessage(message.getMessage());
//            sendMessage(message, service);
//        }
//    }
//
//    public <T> void sendMessage(T message, ChatService service) {
//        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));
//    }
}
