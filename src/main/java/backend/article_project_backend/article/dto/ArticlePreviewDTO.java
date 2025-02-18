package backend.article_project_backend.article.dto;

import java.time.LocalDateTime;

public record ArticlePreviewDTO(
    Integer id,
    String title,
    String abstractionContent,
    String mainImageUrl,
    LocalDateTime createdAt,
    boolean isPremium
) {
    
}
