package backend.article_project_backend.user.mapper;

import backend.article_project_backend.user.dto.UserDTO;
import backend.article_project_backend.user.model.User;

public class UserMapper {
    
    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername());
    }
}
