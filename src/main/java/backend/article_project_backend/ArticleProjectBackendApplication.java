package backend.article_project_backend;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.repository.ArticleRepository;
import backend.article_project_backend.user.model.User;
import backend.article_project_backend.user.repository.UserRepository;
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

    @Autowired
    private UserRepository authorRepository;

	@Override
public void run(String... args) throws Exception {
    // First, create and save 50 authors
    for (int i = 1; i <= 50; i++) {
        User author = new User();
        author.setUsername("Author " + i);  // Unique author full name
        author.setBirthDate(LocalDate.of(1980 + (i % 40), 1 + (i % 12), 1));  // Unique birth date
        author.setSubscriber(i % 2 == 0);  // Alternate subscription status

        // Save to the database
        authorRepository.save(author);

        // Print the saved author ID for reference
        System.out.println("Author " + i + " saved with ID: " + author.getId());
    }

    List<String> tagNames = List.of("Java", "Spring Boot", "Backend");

    System.out.println("Tags saved");
    
    for (int i = 1; i <= 50; i++) {
        Article article = new Article();
        
        // Assign an author to each article
        User author = authorRepository.findById(i)
                .orElseThrow(() -> new RuntimeException("Author not found with ID: "));
                
        article.setUser(author);

        article.setTitle("Spring Boot Article " + i);
        article.setTopic("Technology");
        article.setMainImageUrl("https://example.com/image" + i + ".jpg");
        article.setTags(tagNames); // Assigning preloaded tags
        article.setAbstractContent("This is a summary of article " + i);
        article.setPremium(i % 2 == 0);
        article.setStatus(i % 2 == 0 ? "PUBLISHED" : "DRAFT");
        article.setViews(10 + i);
        article.setCreatedAt(LocalDateTime.now().minusDays(i));

        // Save to the database
        articleRepository.save(article);

        // Print the saved article ID for reference
        System.out.println("Article " + i + " saved with ID: " + article.getId());
    }
}

}
