package lk.uwu.lms_backend.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lk.uwu.lms_backend.dtos.LogoutResponseDTO;
import lk.uwu.lms_backend.dtos.UserAuthResponseDTO;
import lk.uwu.lms_backend.dtos.UserLoginRequestDTO;
import lk.uwu.lms_backend.dtos.UserRegistrationRequestDTO;
import lk.uwu.lms_backend.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    // User Registration
    @PostMapping("/register")
    public ResponseEntity<UserAuthResponseDTO> register(
            @RequestBody UserRegistrationRequestDTO request,
            HttpServletResponse httpServletResponse
    ){
        UserAuthResponseDTO response = authenticationService.registerUser(request, httpServletResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // User Login
    @PostMapping("/login")
    public ResponseEntity<UserAuthResponseDTO> login(
            @RequestBody UserLoginRequestDTO request,
            HttpServletResponse httpServletResponse
    ){
        UserAuthResponseDTO response = authenticationService.loginUser(request, httpServletResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Refresh Token
    @PostMapping("/refresh-token")
    public ResponseEntity<UserAuthResponseDTO> refreshToken(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse httpServletResponse
    ){
        UserAuthResponseDTO response = authenticationService.refreshAccessToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // User Logout
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDTO> logout(
            HttpServletResponse httpServletResponse
    ){
        authenticationService.logoutUser(httpServletResponse);
        LogoutResponseDTO response = new LogoutResponseDTO(
                "User logged out successfully",
                true
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
