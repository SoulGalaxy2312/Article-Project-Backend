package backend.article_project_backend;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.repository.ArticleRepository;
import backend.article_project_backend.user.model.User;
import backend.article_project_backend.user.model.UserRole;
import backend.article_project_backend.user.repository.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ArticleProjectBackendApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(ArticleProjectBackendApplication.class, args);
	}

	@Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

	@Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            List<User> users = new ArrayList<>();
            List<Article> articles = new ArrayList<>();
            Random random = new Random();

            // Generate 50 users
            for (int i = 1; i <= 50; i++) {
                User user = new User();
                user.setUsername("user" + i);
                String encodedPassword = encoder.encode("password" + i);
                user.setPassword(encodedPassword); // Normally, passwords should be encoded
                user.setRole(random.nextBoolean() ? UserRole.ROLE_USER : UserRole.ROLE_ADMIN);
                user.setBirthDate(LocalDate.of(1990 + random.nextInt(30), random.nextInt(12) + 1, random.nextInt(28) + 1));
                user.setSubscriber(random.nextBoolean());
                users.add(user);
            }

            // Save users first
            userRepository.saveAll(users);

            // Generate 50 articles
            for (int i = 1; i <= 50; i++) {
                Article article = new Article();
                article.setUser(users.get(random.nextInt(users.size()))); // Assign a random user
                article.setTitle("Article " + i);
                article.setTopic("Topic " + (random.nextInt(10) + 1));
                article.setMainImageUrl("https://example.com/image" + i + ".jpg");
                article.setTags(List.of("tag" + random.nextInt(5), "tag" + random.nextInt(5)));
                article.setAbstractContent("This is an abstract for article " + i);
                article.setPremium(random.nextBoolean());
                article.setStatus(random.nextBoolean() ? "PUBLISHED" : "DRAFT");
                article.setViews(random.nextInt(1000));
                article.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(365)));
                articles.add(article);
            }

            // Save articles
            articleRepository.saveAll(articles);

            if (!articles.isEmpty()) {
                UUID testArticleId = articles.get(0).getId(); // Get ID of the first article
                System.out.println("Test article ID: " + testArticleId);
            }
            
            System.out.println("50 Users and 50 Articles have been created successfully!");
        };
    }
}
