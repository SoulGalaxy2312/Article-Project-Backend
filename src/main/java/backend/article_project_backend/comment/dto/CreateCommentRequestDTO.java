package backend.article_project_backend.comment.dto;

import java.util.UUID;

import jakarta.annotation.Nullable;

public record CreateCommentRequestDTO(
    String content,

    @Nullable
    UUID parentCommentId
) {
    
}
