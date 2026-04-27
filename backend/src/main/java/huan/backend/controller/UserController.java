package huan.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.UserResponse;
import huan.backend.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUser(){
        return ResponseEntity.ok(userService.getAccount());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getAll(
            @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 10; 
        PageResponse<UserResponse> response = userService.getAllUsers(page, pageSize);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delAccount(@Valid @PathVariable("id") Long id){
        return ResponseEntity.ok(userService.deleteAccount(id));
    }
}