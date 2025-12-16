package lk.uwu.lms_backend.services;

import lk.uwu.lms_backend.dtos.ResponseDTO;
import lk.uwu.lms_backend.dtos.UserDetailsResponseDTO;
import lk.uwu.lms_backend.entities.User;
import lk.uwu.lms_backend.exceptions.UserNotFoundException;
import lk.uwu.lms_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // Get user details by email
    public ResponseDTO<UserDetailsResponseDTO> getUserDetailsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User with email " + email + " not found")
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        UserDetailsResponseDTO userDetailsDTO = new UserDetailsResponseDTO();
        userDetailsDTO.setId(user.getId());
        userDetailsDTO.setFirstName(user.getFirstName());
        userDetailsDTO.setLastName(user.getLastName());
        userDetailsDTO.setEmail(user.getEmail());
        userDetailsDTO.setProfilePhotoUrl(user.getProfilePhotoUrl());
        userDetailsDTO.setRole(user.getRole().name());
        userDetailsDTO.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : null);
        userDetailsDTO.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().format(formatter) : null);

        return new ResponseDTO<>(
                200,
                "User details fetched successfully",
                userDetailsDTO
        );
    }
}
