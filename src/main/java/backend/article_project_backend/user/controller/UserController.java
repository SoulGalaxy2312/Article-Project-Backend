package backend.article_project_backend.user.controller;

import backend.article_project_backend.article.dto.ArticleProfileDTO;
import backend.article_project_backend.article.service.ArticleReadService;
import backend.article_project_backend.user.dto.UserProfileDTO;
import backend.article_project_backend.user.dto.UserProfileWithArticlesDTO;
import backend.article_project_backend.user.service.UserReadService;
import backend.article_project_backend.utils.common.dto.ApiResponse;
import backend.article_project_backend.utils.common.path.AppPaths;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppPaths.USER_URI)
public class UserController {

    private final UserReadService userReadService;
    private final ArticleReadService articleReadService;

    public UserController(UserReadService userReadService, ArticleReadService articleReadService) {
        this.userReadService = userReadService;
        this.articleReadService = articleReadService;        
    }
    
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserProfileWithArticlesDTO> > getProfile() {
        UserProfileDTO userProfileDTO = userReadService.getInfo();
        List<ArticleProfileDTO> articles = articleReadService.getArticleProfile();
        ApiResponse<UserProfileWithArticlesDTO> response = new ApiResponse<>(new UserProfileWithArticlesDTO(userProfileDTO, articles));

        return ResponseEntity.ok().body(response);
    }
}
