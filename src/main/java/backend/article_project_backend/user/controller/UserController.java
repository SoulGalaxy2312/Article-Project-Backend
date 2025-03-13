package backend.article_project_backend.user.controller;

import backend.article_project_backend.user.dto.UserProfileDTO;
import backend.article_project_backend.user.service.UserReadService;
import backend.article_project_backend.utils.common.dto.ApiResponse;
import backend.article_project_backend.utils.common.path.AppPaths;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppPaths.USER_URI)
public class UserController {

    private final UserReadService userReadService;

    public UserController(UserReadService userReadService) {
        this.userReadService = userReadService;
    }
    
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserProfileDTO> > getProfile() {
        UserProfileDTO userProfileDTO = userReadService.getInfo();;
        ApiResponse<UserProfileDTO> response = new ApiResponse<>(userProfileDTO);
        return ResponseEntity.ok().body(response);
    }
}
