package huan.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "NOT_NULL")
    @Email(message = "INVALID_EMAIL")
    private String email;

    @NotBlank(message = "NOT_NULL")
    private String password;
}