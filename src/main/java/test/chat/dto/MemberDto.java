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
public class MemberDto {
    private Long Id;
    private String username;

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .Id(member.getId())
                .username(member.getUsername())
                .build();
    }
}
