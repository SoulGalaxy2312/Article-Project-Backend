package backend.article_project_backend.auth.controller;

import backend.article_project_backend.auth.dto.AuthRequestDTO;
import backend.article_project_backend.auth.dto.RegisterRequestDTO;
import backend.article_project_backend.auth.service.AuthService;
import backend.article_project_backend.utils.common.path.AppPaths;
import lombok.extern.slf4j.Slf4j;
import backend.article_project_backend.utils.common.dto.ApiResponse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(AppPaths.API_BASE_PATH)
@Slf4j
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
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequestDTO request) {
        log.info("Register in controller layer");
        ApiResponse<String> response = new ApiResponse<>(authService.register(request));
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        log.info("Log out in controller layer");
        ApiResponse<String> response = new ApiResponse<>(authService.logout());
        return ResponseEntity.ok().body(response);
    }
    
}
