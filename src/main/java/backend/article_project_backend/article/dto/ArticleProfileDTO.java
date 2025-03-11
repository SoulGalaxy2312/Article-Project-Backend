package backend.article_project_backend.article.dto;

import java.util.List;
import java.util.UUID;

import backend.article_project_backend.article.model.ArticleStatusEnum;

public record ArticleProfileDTO(
    UUID id,

    ArticleStatusEnum status,
    String title,
    String topic,
    String mainImageUrl,

    List<String> tags,
    String abstractContent,
    boolean isPremium
) {
}