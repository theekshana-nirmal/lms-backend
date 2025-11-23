package lk.uwu.lms_backend.repositories;

import lk.uwu.lms_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
