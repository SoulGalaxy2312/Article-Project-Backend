package backend.article_project_backend.user.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import backend.article_project_backend.user.dto.UserProfileDTO;
import backend.article_project_backend.user.mapper.UserMapper;
import backend.article_project_backend.user.model.User;
import backend.article_project_backend.utils.security.authentication.UserPrincipal;

@Service
public class UserReadServiceImpl implements UserReadService {

    @Override
    @PreAuthorize("isAuthenticated()")
    public UserProfileDTO getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object userObject = authentication.getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal) userObject;
        User user = userPrincipal.getUser();

        return UserMapper.toUserProfileDTO(user);
    }
}