package lk.uwu.lms_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseResponseDTO {
    private Long id;
    private String courseName;
    private String description;
    private String coverImageUrl;
    private String createdDate;
    private String updatedAt;
    private UserDetailsResponseDTO createdBy;
}
