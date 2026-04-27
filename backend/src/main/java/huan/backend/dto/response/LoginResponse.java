package huan.backend.dto.response;

import java.time.LocalDateTime;

import huan.backend.enumerate.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
    private Long id;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime issuedAt;
    private Role role;
}
