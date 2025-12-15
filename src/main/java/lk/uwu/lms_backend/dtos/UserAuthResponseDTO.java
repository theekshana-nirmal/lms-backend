package lk.uwu.lms_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAuthResponseDTO {
    private String email;
//    private String accessToken;
    private String role;
}
