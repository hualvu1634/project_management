package huan.backend.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import huan.backend.dto.response.ProjectResponse;
import huan.backend.dto.response.UserResponse;
import huan.backend.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUser(){
        return ResponseEntity.ok(userService.getAccount());
    }

    @GetMapping("/{id}")
    public  ResponseEntity<List<ProjectResponse>> getProjectById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getProjects(id));
    }


}