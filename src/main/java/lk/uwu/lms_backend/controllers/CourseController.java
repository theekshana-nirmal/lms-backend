package lk.uwu.lms_backend.controllers;

import lk.uwu.lms_backend.dtos.CourseResponseDTO;
import lk.uwu.lms_backend.dtos.ResponseDTO;
import lk.uwu.lms_backend.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
