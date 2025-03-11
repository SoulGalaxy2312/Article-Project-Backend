package backend.article_project_backend.article.service;

import backend.article_project_backend.article.dto.CreateArticleRequestDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;

public interface ArticleWriteService {

    FullArticleDTO createArticle(CreateArticleRequestDTO createArticleRequestDTO);
}