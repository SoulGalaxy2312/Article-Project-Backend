package backend.article_project_backend.auth.controller;

import backend.article_project_backend.auth.dto.AuthRequestDTO;
import backend.article_project_backend.auth.service.AuthService;
import backend.article_project_backend.utils.common.path.AppPaths;
import backend.article_project_backend.utils.common.dto.ApiResponse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<String>> login(@RequestBody AuthRequestDTO authRequest) {
        ApiResponse<String> response = new ApiResponse<>(authService.verify(authRequest));
        return ResponseEntity.ok().body(response);
    }
    
}
