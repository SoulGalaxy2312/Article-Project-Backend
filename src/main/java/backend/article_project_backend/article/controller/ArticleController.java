package backend.article_project_backend.article.controller;

import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.service.ArticleService;
import backend.article_project_backend.utils.common.path.AppPaths;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    
    @GetMapping(AppPaths.ARTICLE_URI + "/{id}")
    public FullArticleDTO getSpecificArticle(@PathVariable String id) {
        return articleService.getSpecificArticle(id);    
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
