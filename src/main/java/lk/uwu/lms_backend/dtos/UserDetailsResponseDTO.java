package lk.uwu.lms_backend.dtos;

import lk.uwu.lms_backend.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDetailsResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePhotoUrl;
    private String role;
    private String createdAt;
    private String updatedAt;
}
