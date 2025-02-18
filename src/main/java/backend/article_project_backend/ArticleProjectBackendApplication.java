package backend.article_project_backend;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.repository.ArticleRepository;
import backend.article_project_backend.author.model.Author;
import backend.article_project_backend.author.repository.AuthorRepository;
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
    private AuthorRepository authorRepository;

	@Override
public void run(String... args) throws Exception {
    // First, create and save 50 authors
    for (int i = 1; i <= 50; i++) {
        Author author = new Author();
        author.setFullName("Author " + i);  // Unique author full name
        author.setBirthDate(LocalDate.of(1980 + (i % 40), 1 + (i % 12), 1));  // Unique birth date
        author.setSubscriber(i % 2 == 0);  // Alternate subscription status

        // Save to the database
        authorRepository.save(author);

        // Print the saved author ID for reference
        System.out.println("Author " + i + " saved with ID: " + author.getId());
    }

    // Then, create and save 50 articles, linking each to an author
    for (int i = 1; i <= 50; i++) {
        Article article = new Article();
        
        // Assign the author to each article
        Author author = authorRepository.findById(i).orElseThrow(() -> new Exception("Author not found"));
        article.setAuthor(author);

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
