package backend.article_project_backend.user.dto;

import java.util.List;

import backend.article_project_backend.article.dto.ArticleProfileDTO;

public record UserProfileWithArticlesDTO(
    UserProfileDTO user,
    List<ArticleProfileDTO> articles
) {
}
