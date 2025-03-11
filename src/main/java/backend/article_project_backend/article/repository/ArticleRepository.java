package backend.article_project_backend.article.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.user.model.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID>, JpaSpecificationExecutor<Article> {

    Page<Article> findAllByOrderByViewsDesc(Pageable pageable);
    List<Article> findByUser(User user);
}