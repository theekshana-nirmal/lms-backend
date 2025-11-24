/*
* This class connects 'UserRepository' to Spring Security and tells it how to find a user by email.
* If the user exists, it converts them into a 'CustomUserDetails' object for Spring Security
to check the password and role,
* If not, it gives a warning and stops the login.
* */

package lk.uwu.lms_backend.security;

import lk.uwu.lms_backend.entities.User;
import lk.uwu.lms_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find user in the database
        Optional<User> user = userRepository.findByEmail(email);

        // Check if the user found
        if(user.isEmpty()){
            log.warn("User not found!");
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new CustomUserDetails(user.get());
    }
}
