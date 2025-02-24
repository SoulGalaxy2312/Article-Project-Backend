package backend.article_project_backend.auth.dto;

public record AuthRequestDTO(
    String username,
    String password
) {
}