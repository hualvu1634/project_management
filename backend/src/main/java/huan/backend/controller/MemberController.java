package huan.backend.controller;

import huan.backend.dto.request.MemberRequest;
import huan.backend.dto.response.MailResponse;

import huan.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MailResponse> addMember(@RequestBody MemberRequest request) {
        return new ResponseEntity<>(memberService.addMember(request), HttpStatus.CREATED);
    }
    


    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeMember(@PathVariable Long id) {
    memberService.removeMember(id);
    return ResponseEntity.ok("Xoa thanh cong");
    }
}