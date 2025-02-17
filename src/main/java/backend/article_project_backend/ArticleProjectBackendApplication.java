package backend.article_project_backend;

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
        // Creating an instance using Lombok's @Builder
        Article article = Article.builder()
                .authorId("author123")
                .title("Introduction to Spring Boot")
                .mainCategory("Technology")
                .subCategory("Programming")
                .mainImageUrl("https://example.com/image.jpg")
                .tags(Set.of("Java", "Spring Boot", "Backend"))
                .abstractContent("This article introduces Spring Boot.")
                .isPremium(false)
                .status("PUBLISHED")
                .build();

        // Saving to the database
        articleRepository.save(article);

        System.out.println("Article saved with ID: " + article.getId());
    }
}
