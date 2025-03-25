package backend.article_project_backend.auth.service;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import backend.article_project_backend.auth.dto.AuthRequestDTO;
import backend.article_project_backend.auth.dto.RegisterRequestDTO;
import backend.article_project_backend.user.model.User;
import backend.article_project_backend.user.model.UserRole;
import backend.article_project_backend.user.repository.UserRepository;
import backend.article_project_backend.utils.config.cache.RedisService;
import backend.article_project_backend.utils.security.authentication.UserPrincipal;
import backend.article_project_backend.utils.security.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import backend.article_project_backend.utils.common.cache.RedisKeys;

@Service
@Slf4j
public class AuthService {


    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(
        AuthenticationManager authenticationManager,
        JwtService jwtService,
        RedisService redisService,
        PasswordEncoder passwordEncoder,
        UserRepository userRepository) {

            this.authenticationManager = authenticationManager;
            this.jwtService = jwtService;
            this.redisService = redisService;
            this.passwordEncoder = passwordEncoder;
            this.userRepository = userRepository;
        }

    public String verify(AuthRequestDTO authRequest) {
        Authentication authentication = 
            authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        authRequest.username(), 
                        authRequest.password()));

        if (authentication != null && authentication.isAuthenticated()) {
            log.info("Saving to redis");
            Object userObj = authentication.getPrincipal();
            UserPrincipal userPrincipal = (UserPrincipal) userObj;
            User user = userPrincipal.getUser();
            
            
            String redisKey = RedisKeys.USER_ROLE + user.getUsername();
            log.info("Redis key: " + redisKey);
            redisService.saveData(redisKey, user.getRole());
            log.info("Value: " + user.getRole());
            
            return jwtService.generateToken(authRequest.username());
        } else {
            return "Fail";
        }
    }

    public String register(RegisterRequestDTO request) {
        log.info("Register in service layer");
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.username());

        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        user.setPassword(encodedPassword);
        user.setBirthDate(request.birthDate());
        user.setArticles(new ArrayList<>());
        user.setRole(UserRole.ROLE_USER);

        userRepository.save(user);
        return "Success";
    }

    public String logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String redisKey = RedisKeys.USER_ROLE + username;
        redisService.deleteData(redisKey);
        return "Success";
    }
}
