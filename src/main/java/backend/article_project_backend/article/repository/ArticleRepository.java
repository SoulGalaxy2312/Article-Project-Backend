package backend.article_project_backend.article.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.article_project_backend.article.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Article> findAllByOrderByViewsDesc(Pageable pageable);
}