package huan.backend.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import huan.backend.dto.request.LoginRequest;
import huan.backend.dto.response.LoginResponse;
import huan.backend.entity.User;
import huan.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request){
         
         Authentication authentication = authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
         );
        
        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateAccessToken(request.getEmail());
            String refreshToken = jwtService.generateRefreshToken(request.getEmail());
            User user = userRepository.findByEmail(request.getEmail()).orElse(null);
            return LoginResponse.builder()
            .id(user.getId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .issuedAt(LocalDateTime.now())
                    .role(user.getRole())
                    .build();
        }
        return null;
    }

    public String refreshToken(String refreshToken) {
    String email = jwtService.extractUserName(refreshToken);
    return jwtService.generateAccessToken(email);
}
}