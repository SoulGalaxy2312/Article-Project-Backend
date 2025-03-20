package backend.article_project_backend.article.dto;

import java.time.LocalDateTime;
import java.util.List;

public record FullArticleDTO(
    String id,

    String authorName,

    String title,
    String topic,
    String mainImageUrl,

    List<String> tags,
    String abstractContent,
    boolean isPremium,
    int views,
    LocalDateTime createdAt,
    String content
) {
    
}
