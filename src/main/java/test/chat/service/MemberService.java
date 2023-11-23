package test.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.chat.dto.MemberDto;
import test.chat.entity.Member;
import test.chat.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long createMember(String username) {
        Member member = Member.builder().username(username).build();
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional(readOnly = true)
    public MemberDto findMemberByUsername(String username) {
        Member byUsername = memberRepository.findByUsername(username);
        return MemberDto.toDto(byUsername);
    }
    @Transactional(readOnly = true)
    public MemberDto findMemberById(Long id) {
        Optional<Member> byId = memberRepository.findById(id);
        if (byId.isPresent()) {
            return MemberDto.toDto(byId.get());
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<MemberDto> findAll() {
        List<Member> all = memberRepository.findAll();
        return all.stream().map(MemberDto::toDto).collect(Collectors.toList());
    }
}
