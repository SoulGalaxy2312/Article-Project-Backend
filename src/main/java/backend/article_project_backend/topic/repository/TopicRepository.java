package backend.article_project_backend.topic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.article_project_backend.topic.model.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>{
    Optional<Topic> findByName(String name);
}