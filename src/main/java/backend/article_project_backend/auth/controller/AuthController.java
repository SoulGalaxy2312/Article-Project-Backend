package backend.article_project_backend.auth.controller;

import backend.article_project_backend.auth.dto.AuthRequestDTO;
import backend.article_project_backend.auth.mapper.AuthMapper;
import backend.article_project_backend.auth.service.AuthService;
import backend.article_project_backend.utils.common.path.AppPaths;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(AppPaths.API_BASE_PATH)
public class AuthController {
    
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequestDTO authRequest) {
        return authService.verify(AuthMapper.toEntity(authRequest));
    }
    
}
