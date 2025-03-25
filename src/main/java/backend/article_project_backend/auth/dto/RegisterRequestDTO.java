package backend.article_project_backend.auth.dto;

import java.time.LocalDate;

public record RegisterRequestDTO(
    String username,
    String password,
    LocalDate birthDate
) {
    
}
