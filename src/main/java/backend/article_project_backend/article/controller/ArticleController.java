package backend.article_project_backend.article.controller;

import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.dto.ArticleProfileDTO;
import backend.article_project_backend.article.dto.CreateArticleRequestDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.model.ArticleStatusEnum;
import backend.article_project_backend.article.service.ArticleReadService;

import backend.article_project_backend.article.service.ArticleWriteService;
import backend.article_project_backend.utils.common.dto.ApiResponse;
import backend.article_project_backend.utils.common.path.AppPaths;

import org.springframework.http.ResponseEntity;
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

    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;

    private final Logger logger = Logger.getLogger(ArticleController.class.getName());

    public ArticleController(ArticleReadService articleReadService, ArticleWriteService articleWriteService) {
        this.articleReadService = articleReadService;
        this.articleWriteService = articleWriteService;
    }

    @GetMapping(AppPaths.HOMEPAGE_URI + "/latestArticles")
    public ResponseEntity<ApiResponse<List<ArticlePreviewDTO>>> getHomepageLatestArticles(@RequestParam int pageNumber) {
        return ResponseEntity.ok().body(new ApiResponse<>(articleReadService.getHomepageLatestArticles(pageNumber)));
    }
    
    @GetMapping(AppPaths.HOMEPAGE_URI + "/mostViewedArticles")
    public ResponseEntity<ApiResponse<List<ArticlePreviewDTO>>> getHomepageMostViewedArticles() {
        return ResponseEntity.ok().body(new ApiResponse<>(articleReadService.getHomepageMostViewedArticles()));
    }
    
    // Read a specific article
    @GetMapping(AppPaths.ARTICLE_URI + "/{id}")
    public ResponseEntity<ApiResponse<FullArticleDTO>> getSpecificArticle(@PathVariable UUID id) {
        return ResponseEntity.ok().body(new ApiResponse<>(articleReadService.getSpecificArticle(id)));
    }

    @GetMapping(AppPaths.ARTICLE_URI + "/{id}/relevantArticles")
    public ResponseEntity<ApiResponse<List<ArticlePreviewDTO>>> getRelevantArticles(@PathVariable UUID id) {
        return ResponseEntity.ok().body(new ApiResponse<>(articleReadService.getRelevantArticle(id)));
    }

    @PostMapping(AppPaths.ARTICLE_URI + "/createArticle")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<FullArticleDTO>> createArticle(@ModelAttribute @Validated CreateArticleRequestDTO createArticleRequestDTO) {
        return ResponseEntity.ok().body(new ApiResponse<FullArticleDTO>(articleWriteService.createArticle(createArticleRequestDTO)));        
    }


    @GetMapping(AppPaths.USER_URI + "/articles")
    @PreAuthorize("isAuthenticated()") 
    public ResponseEntity<ApiResponse<List<ArticleProfileDTO>>> getArticlesInProfile(@RequestParam ArticleStatusEnum articleStatus) {
        List<ArticleProfileDTO> articles = articleReadService.getArticleInProfiles(articleStatus);
        ApiResponse<List<ArticleProfileDTO>> response = new ApiResponse<>(articles);
        return ResponseEntity.ok().body(response);
    }
    
}
