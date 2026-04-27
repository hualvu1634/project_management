package huan.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "NOT_NULL")
    @Email(message = "INVALID_EMAIL")
    private String email;

    @Size(min = 4, message = "INVALID_SIZE")
    private String password;

    @NotBlank(message = "NOT_NULL")
    private String name;

    @Pattern(regexp = "^\\d{10}$", message = "INVALID_PHONE")
    private String phoneNumber;
}