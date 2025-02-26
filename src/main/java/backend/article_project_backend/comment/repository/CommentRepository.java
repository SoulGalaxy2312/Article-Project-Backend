package backend.article_project_backend.comment.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.article_project_backend.comment.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByArticleId(UUID id);
}
