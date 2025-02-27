package backend.article_project_backend.utils.security.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import backend.article_project_backend.user.model.User;
import backend.article_project_backend.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = 
            userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user with username: " + username));

        return new UserPrincipal(user);
    }
}
