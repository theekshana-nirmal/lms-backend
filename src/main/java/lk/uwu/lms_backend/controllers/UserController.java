package lk.uwu.lms_backend.controllers;

import lk.uwu.lms_backend.dtos.ResponseDTO;
import lk.uwu.lms_backend.dtos.UserDetailsResponseDTO;
import lk.uwu.lms_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    // Get user details by email
    @GetMapping("/{email}")
    public ResponseEntity<ResponseDTO<UserDetailsResponseDTO>> getUserDetails(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserDetailsByEmail(email));
    }
}
