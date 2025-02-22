package backend.article_project_backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.article_project_backend.user.model.User;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    List<User> findByUsername(String username);
}
