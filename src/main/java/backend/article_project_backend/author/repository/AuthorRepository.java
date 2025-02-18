package backend.article_project_backend.author.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.article_project_backend.author.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    
}
