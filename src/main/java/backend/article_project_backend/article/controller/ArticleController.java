package backend.article_project_backend.article.controller;

import backend.article_project_backend.article.dto.ArticlePagingDTO;
import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.dto.ArticleProfileDTO;
import backend.article_project_backend.article.dto.CreateArticleRequestDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.model.ArticleStatusEnum;
import backend.article_project_backend.article.service.ArticleReadService;

import backend.article_project_backend.article.service.ArticleWriteService;
import backend.article_project_backend.utils.common.dto.ApiResponse;
import backend.article_project_backend.utils.common.path.AppPaths;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@Slf4j
@RestController
public class ArticleController {

    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;

    public ArticleController(ArticleReadService articleReadService, ArticleWriteService articleWriteService) {
        this.articleReadService = articleReadService;
        this.articleWriteService = articleWriteService;
    }

    @GetMapping(AppPaths.HOMEPAGE_URI + "/latestArticles")
    public ResponseEntity<ApiResponse<ArticlePagingDTO>> getHomepageLatestArticles(
        @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber
        ) {
            log.info("Fetching latest articles for homepage with pageNumber = {}", pageNumber);
            ArticlePagingDTO response = articleReadService.getHomepageLatestArticles(pageNumber);
            return ResponseEntity.ok().body(new ApiResponse<>(response));
    }
    
    @GetMapping(AppPaths.HOMEPAGE_URI + "/mostViewedArticles")
    public ResponseEntity<ApiResponse<List<ArticlePreviewDTO>>> getHomepageMostViewedArticles() {
        log.info("Fetching most viewed articles of all time for homepage");
        return ResponseEntity.ok().body(new ApiResponse<>(articleReadService.getHomepageMostViewedArticles()));
    }
    
    // Read a specific article
    @GetMapping(AppPaths.ARTICLE_URI + "/{id}")
    public ResponseEntity<ApiResponse<FullArticleDTO>> getSpecificArticle(@PathVariable UUID id) {
        log.info("Fetching article with id {}", id);
        return ResponseEntity.ok().body(new ApiResponse<>(articleReadService.getSpecificArticle(id)));
    }

    // @GetMapping(AppPaths.ARTICLE_URI + "/{id}/relevantArticles")
    // public ResponseEntity<ApiResponse<List<ArticlePreviewDTO>>> getRelevantArticles(@PathVariable UUID id) {
    //     log.info("Fetching related articles to the article with id ", id);
    //     return ResponseEntity.ok().body(new ApiResponse<>(articleReadService.getRelevantArticle(id)));
    // }

    @PostMapping(value = AppPaths.ARTICLE_URI + "/createArticle", consumes = "multipart/form-data")
    @CrossOrigin(origins = "${client.domain}", allowCredentials = "true")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<FullArticleDTO>> createArticle(@ModelAttribute @Validated CreateArticleRequestDTO createArticleRequestDTO) {
        log.info("Creating new article");
        return ResponseEntity.ok().body(new ApiResponse<FullArticleDTO>(articleWriteService.createArticle(createArticleRequestDTO)));        
    }


    @GetMapping(AppPaths.USER_URI + "/articles")
    @PreAuthorize("isAuthenticated()") 
    public ResponseEntity<ApiResponse<List<ArticleProfileDTO>>> getArticlesInProfile(@RequestParam ArticleStatusEnum articleStatus) {
        log.info("Fetching articles in profile");
        List<ArticleProfileDTO> articles = articleReadService.getArticleInProfiles(articleStatus);
        ApiResponse<List<ArticleProfileDTO>> response = new ApiResponse<>(articles);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(AppPaths.ARTICLE_URI + "/{articleId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> changeStatus(@PathVariable UUID articleId, @RequestParam ArticleStatusEnum status) {
        log.info("Controller layer: change status artice");
        String response = articleWriteService.changeStatus(articleId, status);
        
        return ResponseEntity.ok().body(new ApiResponse<String>(response));
    }
}
