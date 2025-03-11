package backend.article_project_backend.article.service;

import java.util.List;
import java.util.UUID;

import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;

public interface ArticleReadService {

    List<ArticlePreviewDTO> getHomepageLatestArticles(int pageNumber);
    List<ArticlePreviewDTO> getHomepageMostViewedArticles();
    FullArticleDTO getSpecificArticle(UUID id);
    public List<ArticlePreviewDTO> getRelevantArticle(UUID id);
}
