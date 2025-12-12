package lk.uwu.lms_backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lk.uwu.lms_backend.dtos.CourseRequestDTO;
import lk.uwu.lms_backend.dtos.CourseResponseDTO;
import lk.uwu.lms_backend.dtos.ResponseDTO;
import lk.uwu.lms_backend.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/courses")
public class CourseController {
    private final CourseService courseService;

    // Get All Courses
    @GetMapping
    public ResponseEntity<ResponseDTO<List<CourseResponseDTO>>> getAllCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getAllCourses());
    }

    // Create a Course
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<CourseResponseDTO>> createCourse(
            @Validated @RequestBody CourseRequestDTO request,
            HttpServletRequest httpServletRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(request));
    }
}
