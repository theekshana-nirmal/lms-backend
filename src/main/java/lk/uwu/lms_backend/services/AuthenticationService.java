package lk.uwu.lms_backend.services;

import lk.uwu.lms_backend.dtos.UserAuthResponseDTO;
import lk.uwu.lms_backend.dtos.UserRegistrationRequestDTO;
import lk.uwu.lms_backend.entities.CustomUserDetails;
import lk.uwu.lms_backend.entities.User;
import lk.uwu.lms_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Step 1: Implement user registration
    public UserAuthResponseDTO registerUser(UserRegistrationRequestDTO request){
        var user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setHashedPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        // Save user to the database
        User savedUser = userRepository.save(user);

        // Create UserDetails object
        CustomUserDetails userDetails = new CustomUserDetails(savedUser);

        // Generate JWT Access Token
        String jwtToken = jwtService.generateAccessToken(new HashMap<>(), userDetails);

        // Generate Refresh Token
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), userDetails);

        return new UserAuthResponseDTO(
                savedUser.getEmail(),
                jwtToken,
                refreshToken,
                savedUser.getRole().name(),
                TimeUnit.MINUTES.toMillis(30)
        );
    }

}
