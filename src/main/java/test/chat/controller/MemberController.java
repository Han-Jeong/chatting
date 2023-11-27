package test.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import test.chat.dto.MemberDTO;
import test.chat.service.MemberService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("member/create")
    public ResponseEntity createMember(@Param("username") String username) {
        Long member = memberService.createMember(username);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @GetMapping("member/List")
    public ResponseEntity findMembers() {
        List<MemberDTO> all = memberService.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
}
