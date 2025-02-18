package backend.article_project_backend.article.model;

import java.time.LocalDateTime;
import java.util.Set;

import backend.article_project_backend.author.model.Author;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Article {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    private String title;
    private String topic;
    private String mainImageUrl;

    private Set<String> tags;

    private String abstractContent;
    private boolean isPremium;
    private String status;
    private int views;

    private LocalDateTime createdAt;
}
