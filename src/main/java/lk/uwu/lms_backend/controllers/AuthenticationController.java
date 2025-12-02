package lk.uwu.lms_backend.controllers;

import lk.uwu.lms_backend.dtos.UserAuthResponseDTO;
import lk.uwu.lms_backend.dtos.UserLoginRequestDTO;
import lk.uwu.lms_backend.dtos.UserRegistrationRequestDTO;
import lk.uwu.lms_backend.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    // User Registration
    @PostMapping("/register")
    public ResponseEntity<UserAuthResponseDTO> register(
            @RequestBody UserRegistrationRequestDTO request
    ){
        UserAuthResponseDTO response = authenticationService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // User Login
    @PostMapping("/login")
    public ResponseEntity<UserAuthResponseDTO> login(
            @RequestBody UserLoginRequestDTO request
    ){
        UserAuthResponseDTO response = authenticationService.loginUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
