package backend.article_project_backend.article.controller;

import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.service.ArticleService;
import backend.article_project_backend.utils.common.path.AppPaths;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(AppPaths.API_BASE_PATH + "getHompageArticlePreviews")
    public List<ArticlePreviewDTO> getMethodName(@RequestParam int pageNumber) {

        return articleService.getArticlesPreviewByPage(pageNumber);
    }
    
}
