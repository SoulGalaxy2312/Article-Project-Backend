package backend.article_project_backend.article.controller;

import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.service.ArticleService;
import backend.article_project_backend.utils.common.path.AppPaths;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(AppPaths.HOMEPAGE_URI + "/latestArticles")
    public List<ArticlePreviewDTO> getMethodName(@RequestParam int pageNumber) {

        return articleService.getArticlesPreviewByPage(pageNumber);
    }
    
    @GetMapping(AppPaths.HOMEPAGE_URI + "/tenMostViewedArticles")
    public List<ArticlePreviewDTO> getMethodName() {
        return articleService.getTenArticlesWithMostViews();
    }
    
    // Read a specific article
    @GetMapping(AppPaths.ARTICLE_URI + "/{id}")
    public FullArticleDTO getSpecificArticle(@PathVariable UUID id) {
        return articleService.getSpecificArticle(id);    
    }

    @GetMapping(AppPaths.ARTICLE_URI + "/{id}/relevantArticles")
    public List<ArticlePreviewDTO> getRelevantArticles(@PathVariable UUID id) {
        return articleService.getRelevantArticle(id);
    }
}
