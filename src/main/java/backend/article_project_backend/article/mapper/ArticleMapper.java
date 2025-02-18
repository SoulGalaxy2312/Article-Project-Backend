package backend.article_project_backend.article.mapper;

import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.model.Article;

public class ArticleMapper {
    
    private ArticleMapper() {}

    public static ArticlePreviewDTO toDTO(Article article) {
        return new ArticlePreviewDTO(
            article.getId(), 
            article.getTitle(), 
            article.getAbstractContent(), 
            article.getMainImageUrl(), 
            article.getCreatedAt(), 
            article.isPremium(),
            article.getAuthor().getFullName());
    }
}
