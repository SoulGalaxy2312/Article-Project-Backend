package backend.article_project_backend.user.service;

import backend.article_project_backend.user.dto.UserProfileDTO;

public interface UserReadService {
    
    UserProfileDTO getInfo();
}
