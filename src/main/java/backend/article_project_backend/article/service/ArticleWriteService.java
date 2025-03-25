package backend.article_project_backend.article.service;

import java.util.UUID;

import backend.article_project_backend.article.dto.CreateArticleRequestDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.model.ArticleStatusEnum;

public interface ArticleWriteService {

    FullArticleDTO createArticle(CreateArticleRequestDTO createArticleRequestDTO);
    String changeStatus(UUID articleId, ArticleStatusEnum status);
}