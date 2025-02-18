package backend.article_project_backend;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.repository.ArticleRepository;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ArticleProjectBackendApplication implements CommandLineRunner {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(ArticleProjectBackendApplication.class, args);
	}

	@Autowired
    private ArticleRepository articleRepository;

	@Override
    public void run(String... args) throws Exception {
        // Loop to create and save 50 instances of Article
        for (int i = 1; i <= 50; i++) {
            Article article = new Article();
            article.setAuthorId("author" + i);  // Unique authorId
            article.setTitle("Spring Boot Article " + i);  // Unique title
            article.setTopic("Technology");
            article.setMainImageUrl("https://example.com/image" + i + ".jpg");  // Unique image URL
            article.setTags(Set.of("Java", "Spring Boot", "Backend"));
            article.setAbstractContent("This is a summary of article " + i);
            article.setPremium(i % 2 == 0);
            article.setStatus(i % 2 == 0 ? "PUBLISHED" : "DRAFT");  // Alternate status
            article.setViews(10 + i);  // Unique view count (starting from 11)
            article.setCreatedAt(LocalDateTime.now().minusDays(i));  // Set createdAt to a past date

            // Save to the database
            articleRepository.save(article);

            // Print the saved article ID for reference
            System.out.println("Article " + i + " saved with ID: " + article.getId());
        }
    }
}
