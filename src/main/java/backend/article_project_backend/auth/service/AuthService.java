package backend.article_project_backend.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import backend.article_project_backend.auth.dto.AuthRequestDTO;
import backend.article_project_backend.user.model.User;
import backend.article_project_backend.utils.config.cache.RedisService;
import backend.article_project_backend.utils.security.authentication.UserPrincipal;
import backend.article_project_backend.utils.security.jwt.JwtService;
import backend.article_project_backend.utils.common.cache.RedisKeys;

@Service
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisService redisService;

    public AuthService(
        AuthenticationManager authenticationManager,
        JwtService jwtService,
        RedisService redisService) {

            this.authenticationManager = authenticationManager;
            this.jwtService = jwtService;
            this.redisService = redisService;
        }

    public String verify(AuthRequestDTO authRequest) {
        Authentication authentication = 
            authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        authRequest.username(), 
                        authRequest.password()));

        if (authentication != null && authentication.isAuthenticated()) {
            Object userObj = authentication.getPrincipal();
            UserPrincipal userPrincipal = (UserPrincipal) userObj;
            User user = userPrincipal.getUser();
            
            String redisKey = RedisKeys.USER_ROLE + user.getUsername();
            redisService.saveData(redisKey, user.getRole().toString());
            
            return jwtService.generateToken(authRequest.username());
        } else {
            return "Fail";
        }
    }
}
