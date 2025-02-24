package backend.article_project_backend.auth.mapper;

import backend.article_project_backend.auth.dto.AuthRequestDTO;
import backend.article_project_backend.user.model.User;

public class AuthMapper {
    
    public static User toEntity(AuthRequestDTO authRequestDTO) {
        User user = new User();
        user.setUsername(authRequestDTO.username());
        user.setPassword(authRequestDTO.password());

        return user;
    }
}
