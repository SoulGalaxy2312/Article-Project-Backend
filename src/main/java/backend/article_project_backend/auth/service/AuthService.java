package backend.article_project_backend.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import backend.article_project_backend.user.model.User;
import backend.article_project_backend.user.repository.UserRepository;
import backend.article_project_backend.utils.security.jwt.JwtService;

@Service
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
        UserRepository userRepository, 
        AuthenticationManager authenticationManager,
        JwtService jwtService) {

            this.authenticationManager = authenticationManager;
            this.jwtService = jwtService;
        }

    public String verify(User user) {
        Authentication authentication = 
            authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        user.getUsername(), 
                        user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "Fail";
        }
    }
}
