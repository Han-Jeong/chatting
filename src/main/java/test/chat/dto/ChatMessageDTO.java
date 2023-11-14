package test.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import test.chat.entity.ChatMessage;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private Long Id;
    private Long roomId; // 방 번호
    private String sender; // 채팅을 보낸 사람
    private String message; // 메시지
    private String time; // 채팅 발송 시간간

    public static ChatMessageDTO toDto(ChatMessage message) {
        return ChatMessageDTO.builder()
                .message(message.getContent())
                .Id(message.getId())
                .roomId(message.getChatRoom().getId())
                .sender(message.getSender().getUsername())
                .build();

    }
}
