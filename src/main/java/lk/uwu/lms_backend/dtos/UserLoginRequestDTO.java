package lk.uwu.lms_backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserLoginRequestDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
