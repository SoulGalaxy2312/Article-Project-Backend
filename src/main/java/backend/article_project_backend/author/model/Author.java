package backend.article_project_backend.author.model;

import java.time.LocalDate;
import java.util.List;

import backend.article_project_backend.article.model.Article;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author {
    
    @Id
    @GeneratedValue
    private Integer id;

    private String fullName;

    private LocalDate birthDate;

    private boolean isSubscriber;

    @OneToMany(mappedBy = "author")
    private List<Article> articles;
}
