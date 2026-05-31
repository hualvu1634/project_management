package huan.backend.dto.response;


import huan.backend.enums.Role;
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
    private Role role;
}
