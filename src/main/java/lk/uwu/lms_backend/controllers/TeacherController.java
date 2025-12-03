package lk.uwu.lms_backend.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/dashboard")
    public String teacherDashboard() {
        return "Welcome to the Teacher Dashboard!";
    }
}
