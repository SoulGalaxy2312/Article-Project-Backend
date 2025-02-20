package backend.article_project_backend.article.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ArticlePreviewDTO(
    UUID id,
    String title,
    String abstractionContent,
    String mainImageUrl,
    LocalDateTime createdAt,
    boolean isPremium,
    String createdBy
) {
    
}
