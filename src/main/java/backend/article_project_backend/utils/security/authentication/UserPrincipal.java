package backend.article_project_backend.utils.security.authentication;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import backend.article_project_backend.user.model.User;

public class UserPrincipal implements UserDetails {
    
    private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }
    
    public String getUsername() {
        return this.user.getUsername();
    }

    public String getPassword() {
        return this.user.getPassword();
    }

    // 22.2.2025 only has 1 user role
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
