package lk.uwu.lms_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
}
