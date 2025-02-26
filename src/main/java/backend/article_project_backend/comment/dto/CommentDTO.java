package backend.article_project_backend.comment.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import backend.article_project_backend.user.dto.UserDTO;

public record CommentDTO(
    UUID id,
    UserDTO user,
    String content,
    LocalDateTime createdAt,
    List<CommentDTO> replies
) {
} 