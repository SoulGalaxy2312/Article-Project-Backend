package backend.article_project_backend.user.dto;

import java.time.LocalDate;

public record UserProfileDTO(
    String username,
    LocalDate birthDate,
    String role
) {
}