package lk.uwu.lms_backend.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lk.uwu.lms_backend.dtos.UserAuthResponseDTO;
import lk.uwu.lms_backend.dtos.UserLoginRequestDTO;
import lk.uwu.lms_backend.dtos.UserRegistrationRequestDTO;
import lk.uwu.lms_backend.entities.CustomUserDetails;
import lk.uwu.lms_backend.entities.User;
import lk.uwu.lms_backend.exceptions.UserAlreadyExistsException;
import lk.uwu.lms_backend.exceptions.UserCredentialsInvalidException;
import lk.uwu.lms_backend.exceptions.UserNotFoundException;
import lk.uwu.lms_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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

    // User registration
    public UserAuthResponseDTO registerUser(UserRegistrationRequestDTO request, HttpServletResponse response) {
        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setHashedPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        // Save user to the database
        User savedUser = userRepository.save(user);
        return authResponse(savedUser, response);
    }

    // User Login
    public UserAuthResponseDTO loginUser(UserLoginRequestDTO request, HttpServletResponse response) throws UserCredentialsInvalidException {
        try {
            // Authenticate user credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (AuthenticationException e) {
            throw new UserCredentialsInvalidException("Invalid email or password");
        }

        // Retrieve user from the database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + request.getEmail() + " not found"));
        return authResponse(user, response);
    }

    // Make authentication response with JWT tokens
    private UserAuthResponseDTO authResponse(User user, HttpServletResponse response) {
        // Convert to UserDetails
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Generate JWT Tokens
        String accessToken = jwtService.generateAccessToken(new HashMap<>(), userDetails);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), userDetails);

        // Set Refresh Token as a HttpOnly cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        // refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(30));
        response.addCookie(refreshTokenCookie);

        return new UserAuthResponseDTO(
                user.getEmail(),
                accessToken,
                user.getRole().name(),
                TimeUnit.MINUTES.toMillis(30));
    }

}
