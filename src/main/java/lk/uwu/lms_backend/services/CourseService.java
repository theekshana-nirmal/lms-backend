package lk.uwu.lms_backend.services;

import lk.uwu.lms_backend.dtos.CourseRequestDTO;
import lk.uwu.lms_backend.dtos.CourseResponseDTO;
import lk.uwu.lms_backend.dtos.ResponseDTO;
import lk.uwu.lms_backend.dtos.UserDetailsResponseDTO;
import lk.uwu.lms_backend.entities.Course;
import lk.uwu.lms_backend.entities.User;
import lk.uwu.lms_backend.exceptions.UserNotFoundException;
import lk.uwu.lms_backend.repositories.CourseRepository;
import lk.uwu.lms_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
        private final CourseRepository courseRepository;
        private final UserRepository userRepository;

        // GET ALL COURSES
        public ResponseDTO<List<CourseResponseDTO>> getAllCourses() {
                List<Course> courses = courseRepository.findAll();

                if (courses.isEmpty()) {
                        return new ResponseDTO<>(
                                        200,
                                        "No courses found",
                                        List.of());
                }

                // Convert List<Course> -> List<CourseResponseDTO>
                List<CourseResponseDTO> courseDTOS = courses.stream().map(this::mapToDto).toList();

                return new ResponseDTO<>(
                                200,
                                "Success",
                                courseDTOS);
        }

        // CREATE A COURSE
        public ResponseDTO<CourseResponseDTO> createCourse(CourseRequestDTO request) {
                // Get currently authenticated user (the teacher creating the course)
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String teacherEmail = authentication.getName(); // Gets the email from UserDetails

                // Find the teacher in the database
                User teacher = userRepository.findByEmail(teacherEmail)
                                .orElseThrow(() -> new UserNotFoundException("Teacher not found"));

                Course course = new Course();

                course.setCourseName(request.getCourseName());
                course.setDescription(request.getDescription());
                course.setCoverImageUrl(
                                request.getCoverImageUrl() != null ? request.getCoverImageUrl()
                                                : "https://placehold.co/600x400.jpeg");
                course.setCreatedBy(teacher); 

                Course savedCourse = courseRepository.save(course);

                CourseResponseDTO courseDto = mapToDto(savedCourse);

                return new ResponseDTO<>(
                                201,
                                "Course created successfully",
                                courseDto);
        }

        // UPDATE A COURSE
        public ResponseDTO<CourseResponseDTO> updateCourse(Long courseId, CourseRequestDTO request) {
                Course course = courseRepository.findById(courseId)
                                .orElseThrow(() -> new RuntimeException("Course not found"));

                course.setCourseName(request.getCourseName());
                course.setDescription(request.getDescription());
                course.setCoverImageUrl(
                                request.getCoverImageUrl() != null ? request.getCoverImageUrl()
                                                : course.getCoverImageUrl());

                Course updatedCourse = courseRepository.save(course);
                CourseResponseDTO courseDto = mapToDto(updatedCourse);

                return new ResponseDTO<>(
                                200,
                                "Course updated successfully",
                                courseDto);
        }

        // Utility for Map to DTO
        private CourseResponseDTO mapToDto(Course course) {
                CourseResponseDTO courseDto = new CourseResponseDTO();

                courseDto.setId(course.getId());
                courseDto.setCourseName(course.getCourseName());
                courseDto.setDescription(course.getDescription());
                courseDto.setCoverImageUrl(
                                course.getCoverImageUrl() != null ? course.getCoverImageUrl()
                                                : "https://placehold.co/600x400.jpeg");
                courseDto.setCreatedDate(course.getCreatedDate().toString());
                courseDto.setUpdatedAt(course.getUpdatedAt().toString());

                // Covert User Details to DTO
                if (course.getCreatedBy() != null) {
                        UserDetailsResponseDTO userDTO = new UserDetailsResponseDTO();
                        User user = course.getCreatedBy();

                        userDTO.setId(user.getId());
                        userDTO.setFirstName(user.getFirstName());
                        userDTO.setLastName(user.getLastName());
                        userDTO.setEmail(user.getEmail());
                        userDTO.setRole(user.getRole().toString());
                        userDTO.setProfilePhotoUrl(user.getProfilePhotoUrl());
                        userDTO.setCreatedAt(user.getCreatedAt().toString());
                        userDTO.setUpdatedAt(
                                        user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null);

                        courseDto.setCreatedBy(userDTO);
                }

                return courseDto;
        }
}
