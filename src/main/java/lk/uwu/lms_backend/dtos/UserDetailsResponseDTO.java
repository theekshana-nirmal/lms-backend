package lk.uwu.lms_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
