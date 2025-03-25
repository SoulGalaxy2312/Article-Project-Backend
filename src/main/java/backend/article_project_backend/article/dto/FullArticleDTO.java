package backend.article_project_backend.article.dto;

import java.time.LocalDateTime;
import java.util.List;

import backend.article_project_backend.topic.model.Topic;

public record FullArticleDTO(
    String id,

    String authorName,

    String title,
    Topic topic,
    String mainImageUrl,

    List<String> tags,
    String abstractContent,
    boolean isPremium,
    int views,
    LocalDateTime createdAt,
    String content
) {
    
}
