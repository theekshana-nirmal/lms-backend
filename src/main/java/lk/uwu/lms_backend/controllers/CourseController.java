package lk.uwu.lms_backend.controllers;

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
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    // Get All Courses
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    @GetMapping
    public ResponseEntity<ResponseDTO<List<CourseResponseDTO>>> getAllCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getAllCourses());
    }

    // Get Course by ID
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    @GetMapping("/{courseId}")
    public ResponseEntity<ResponseDTO<CourseResponseDTO>> getCourseById(
            @PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourseById(courseId));
    }

    // Get Courses by Teacher ID
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ResponseDTO<List<CourseResponseDTO>>> getCoursesByTeacher(
            @PathVariable Long teacherId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getCoursesByTeacher(teacherId));
    }

    // Create a Course
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<CourseResponseDTO>> createCourse(
            @Validated @RequestBody CourseRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(request));
    }

    // Update a Course
    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update/{courseId}")
    public ResponseEntity<ResponseDTO<CourseResponseDTO>> updateCourse(
            @PathVariable Long courseId,
            @Validated @RequestBody CourseRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.updateCourse(courseId, request));
    }

    // Delete a Course
    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<ResponseDTO<String>> deleteCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.deleteCourse(courseId));
    }
}