package backend.article_project_backend.comment.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateCommentResponseDTO(
    boolean isSuccess,
    String message,
    UUID createdCommentId,
    LocalDateTime createdAt
) {
}