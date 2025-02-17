package backend.article_project_backend.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.article_project_backend.article.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
}