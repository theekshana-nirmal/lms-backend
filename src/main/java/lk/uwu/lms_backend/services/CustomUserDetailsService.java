/*
* This class connects 'UserRepository' to Spring Security and tells it how to find a user by email.
* If the user exists, it converts them into a 'CustomUserDetails' object for Spring Security
to check the password and role,
* If not, it gives a warning and stops the login.
* */

package lk.uwu.lms_backend.services;

import lk.uwu.lms_backend.entities.User;
import lk.uwu.lms_backend.repositories.UserRepository;
import lk.uwu.lms_backend.entities.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find user in the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));

        return new CustomUserDetails(user);
    }
}
