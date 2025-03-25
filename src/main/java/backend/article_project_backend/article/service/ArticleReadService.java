package backend.article_project_backend.article.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import backend.article_project_backend.article.dto.ArticlePagingDTO;
import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.dto.ArticleProfileDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.model.ArticleStatusEnum;

public interface ArticleReadService {

    ArticlePagingDTO fetchArticles(Specification<Article> spec, int pageNumber, int pageSize, boolean isHomepageRequest);
    ArticlePagingDTO getHomepageLatestArticles(int pageNumber);
    List<ArticlePreviewDTO> getHomepageMostViewedArticles();
    FullArticleDTO getSpecificArticle(UUID id);
    List<ArticlePreviewDTO> getRelevantArticle(UUID id);

    List<ArticleProfileDTO> getArticleInProfiles(ArticleStatusEnum articleStatus);
}
