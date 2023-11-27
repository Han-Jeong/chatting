package test.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import test.chat.entity.Member;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private Long Id;
    private String username;

    public static MemberDTO toDto(Member member) {
        return MemberDTO.builder()
                .Id(member.getId())
                .username(member.getUsername())
                .build();
    }
}
