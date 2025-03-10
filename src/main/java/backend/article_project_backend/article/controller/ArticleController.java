package backend.article_project_backend.article.controller;

import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.dto.CreateArticleRequestDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.service.ArticleService;
import backend.article_project_backend.utils.common.dto.ApiResponse;
import backend.article_project_backend.utils.common.path.AppPaths;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class ArticleController {
    private final ArticleService articleService;

    private final Logger logger = Logger.getLogger(ArticleController.class.getName());

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(AppPaths.HOMEPAGE_URI + "/latestArticles")
    public ApiResponse<List<ArticlePreviewDTO>> getHomepageLatestArticles(@RequestParam int pageNumber) {
        return new ApiResponse<>(articleService.getHomepageLatestArticles(pageNumber));
    }
    
    @GetMapping(AppPaths.HOMEPAGE_URI + "/mostViewedArticles")
    public ApiResponse<List<ArticlePreviewDTO>> getHomepageMostViewedArticles() {
        return new ApiResponse<>(articleService.getHomepageMostViewedArticles());
    }
    
    // Read a specific article
    @GetMapping(AppPaths.ARTICLE_URI + "/{id}")
    public ApiResponse<FullArticleDTO> getSpecificArticle(@PathVariable UUID id) {
        return new ApiResponse<>(articleService.getSpecificArticle(id));    
    }

    @GetMapping(AppPaths.ARTICLE_URI + "/{id}/relevantArticles")
    public ApiResponse<List<ArticlePreviewDTO>> getRelevantArticles(@PathVariable UUID id) {
        return new ApiResponse<>(articleService.getRelevantArticle(id));
    }

    @PostMapping(AppPaths.ARTICLE_URI + "/createArticle")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<FullArticleDTO> createArticle(@ModelAttribute @Validated CreateArticleRequestDTO createArticleRequestDTO) {
        return new ApiResponse<FullArticleDTO>(articleService.createArticle(createArticleRequestDTO));        
    }
    
}
