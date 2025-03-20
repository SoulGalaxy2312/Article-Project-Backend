package backend.article_project_backend.article.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import backend.article_project_backend.topic.model.Topic;
import backend.article_project_backend.user.model.User;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
    
    private String mainImageUrl;
    
    @ElementCollection
    private List<String> tags = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private ArticleStatusEnum status;

    private String abstractContent;
    private boolean isPremium;
    private int views;
    private LocalDateTime createdAt;

    private String content;
}
