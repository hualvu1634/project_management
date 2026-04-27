package huan.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import huan.backend.dto.request.LoginRequest;
import huan.backend.dto.request.UserRequest;
import huan.backend.dto.response.LoginResponse;
import huan.backend.dto.response.UserResponse;
import huan.backend.service.AuthenticationService;
import huan.backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService; 
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.addAccount(userRequest), HttpStatus.CREATED);
    }
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody String refreshToken) {
    return ResponseEntity.ok(authenticationService.refreshToken(refreshToken));
}
}