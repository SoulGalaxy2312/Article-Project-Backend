package backend.article_project_backend.article.mapper;

import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.model.Article;

public class ArticleMapper {
    
    private ArticleMapper() {}

    public static ArticlePreviewDTO toArticlePreviewDTO(Article article) {
        return new ArticlePreviewDTO(
            article.getId(), 
            article.getTitle(), 
            article.getAbstractContent(), 
            article.getMainImageUrl(), 
            article.getCreatedAt(), 
            article.isPremium(),
            article.getUser().getUsername());
    }

    public static FullArticleDTO toFullArticleDTO(Article article) {
        return new FullArticleDTO(
            article.getId().toString(), 
            article.getUser().getUsername(), 
            article.getTitle(), 
            article.getTopic(), 
            article.getMainImageUrl(), 
            article.getTags(), 
            article.getAbstractContent(), 
            article.isPremium(), 
            article.getViews(), 
            article.getCreatedAt());
    }
}
