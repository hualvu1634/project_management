package huan.backend.controller;

import huan.backend.dto.request.MemberRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.MemberProjectResponse;
import huan.backend.dto.response.MemberResponse;
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
    public ResponseEntity<MemberResponse> addMember(@RequestBody MemberRequest request) {
        return new ResponseEntity<>(memberService.addMember(request), HttpStatus.CREATED);
    }
    
    @GetMapping("/project/{projectId}")
    public ResponseEntity<MemberProjectResponse> getMembersByProject(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(memberService.getMembersByProject(projectId, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> removeMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.removeMember(id));
    }
}