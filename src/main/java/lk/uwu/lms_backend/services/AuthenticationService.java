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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // User registration
    public UserAuthResponseDTO registerUser(UserRegistrationRequestDTO request, HttpServletResponse response) {
        // Check if passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new UserCredentialsInvalidException("Passwords do not match");
        }
        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with provided email already exists");
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

        log.info("Generated Access Token: {}", accessToken);
        log.info("Generated Refresh Token: {}", refreshToken);
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

    // Get new Access Token using Refresh Token
    public UserAuthResponseDTO refreshAccessToken(String refreshToken, HttpServletResponse response) {
        // Validate Refresh Token
        String userEmail = jwtService.extractUserEmail(refreshToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email " + userEmail + " not found"));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new UserCredentialsInvalidException("Invalid refresh token");
        }

        // Generate new Access Token
        String newAccessToken = jwtService.generateAccessToken(new HashMap<>(), userDetails);

        return new UserAuthResponseDTO(
                user.getEmail(),
                newAccessToken,
                user.getRole().name(),
                TimeUnit.MINUTES.toMillis(30));
    }

    // User Logout
    public void logoutUser(HttpServletResponse response) {
        // Invalidate the refresh token cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        // refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(0); // Expire immediately
        response.addCookie(refreshTokenCookie);
    }
}
