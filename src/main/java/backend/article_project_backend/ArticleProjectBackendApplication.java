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
import org.springframework.security.crypto.password.PasswordEncoder;

import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.model.ArticleStatusEnum;
import backend.article_project_backend.article.repository.ArticleRepository;
import backend.article_project_backend.comment.model.Comment;
import backend.article_project_backend.comment.repository.CommentRepository;
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
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder encoder;

	@Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            List<User> users = new ArrayList<>();
            List<Article> articles = new ArrayList<>();
            List<Comment> comments = new ArrayList<>();
            Random random = new Random();

            User user1 = new User();
            user1.setUsername("user1");
            String encodedPassword = encoder.encode("password");
            user1.setPassword(encodedPassword); 
            user1.setRole(UserRole.ROLE_USER);
            user1.setBirthDate(LocalDate.of(1990 + random.nextInt(30), random.nextInt(12) + 1, random.nextInt(28) + 1));
            users.add(user1);
            
            User user2 = new User();
            user2.setUsername("user2");
            user2.setPassword(encodedPassword); 
            user2.setRole(UserRole.ROLE_USER);
            user2.setBirthDate(LocalDate.of(1990 + random.nextInt(30), random.nextInt(12) + 1, random.nextInt(28) + 1));
            users.add(user2);

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encodedPassword); 
            admin.setRole(UserRole.ROLE_ADMIN);
            admin.setBirthDate(LocalDate.of(1990 + random.nextInt(30), random.nextInt(12) + 1, random.nextInt(28) + 1));
            users.add(admin);

            // Save users first
            userRepository.saveAll(users);

            ArticleStatusEnum[] statuses = {ArticleStatusEnum.PENDING, ArticleStatusEnum.REJECTED, ArticleStatusEnum.PUBLISHED};
            // Generate 20 articles
            for (int i = 1; i <= 20; i++) {
                Article article = new Article();
                article.setUser(random.nextBoolean() ? user1 : user2); // Assign a random user
                article.setTitle("Article " + i);
                article.setTopic("Topic " + (random.nextInt(10) + 1));
                article.setMainImageUrl("https://example.com/image" + i + ".jpg");
                article.setTags(List.of("tag" + random.nextInt(5), "tag" + random.nextInt(5)));
                article.setAbstractContent("This is an abstract for article " + i);
                article.setPremium(random.nextBoolean());
                article.setStatus(statuses[random.nextInt(statuses.length)]);
                article.setViews(random.nextInt(1000));
                article.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(365)));
                articles.add(article);
            }

            // Save articles
            articleRepository.saveAll(articles);

            // Select a specific article for comments
            Article selectedArticle = articles.get(0); // Use the first article
            UUID testArticleId = selectedArticle.getId();

            // Generate 30 comments with a 4-level tree structure
            List<Comment> level1 = new ArrayList<>();
            List<Comment> level2 = new ArrayList<>();
            List<Comment> level3 = new ArrayList<>();
            List<Comment> level4 = new ArrayList<>();

            // Generate level-1 comments (top-level, no parent)
            for (int i = 1; i <= 5; i++) {
                Comment comment = new Comment();
                comment.setArticle(selectedArticle);
                comment.setUser(users.get(random.nextInt(users.size())));
                comment.setContent("Level 1 Comment " + i);
                comment.setCreatedAt(LocalDateTime.now().minusHours(random.nextInt(24)));
                comment.setParent(null);
                level1.add(comment);
            }

            comments.addAll(level1);

            // Generate level-2 comments (replies to level-1)
            for (Comment parent : level1) {
                for (int i = 1; i <= 3; i++) {
                    Comment comment = new Comment();
                    comment.setArticle(selectedArticle);
                    comment.setUser(users.get(random.nextInt(users.size())));
                    comment.setContent("Level 2 Reply to " + parent.getId());
                    comment.setCreatedAt(LocalDateTime.now().minusHours(random.nextInt(24)));
                    comment.setParent(parent);
                    level2.add(comment);
                }
            }

            comments.addAll(level2);

            // Generate level-3 comments (replies to level-2)
            for (Comment parent : level2) {
                for (int i = 1; i <= 2; i++) {
                    Comment comment = new Comment();
                    comment.setArticle(selectedArticle);
                    comment.setUser(users.get(random.nextInt(users.size())));
                    comment.setContent("Level 3 Reply to " + parent.getId());
                    comment.setCreatedAt(LocalDateTime.now().minusHours(random.nextInt(24)));
                    comment.setParent(parent);
                    level3.add(comment);
                }
            }

            comments.addAll(level3);

            // Generate level-4 comments (replies to level-3)
            for (Comment parent : level3) {
                for (int i = 1; i <= 1; i++) { // Only one reply per level-3 comment
                    Comment comment = new Comment();
                    comment.setArticle(selectedArticle);
                    comment.setUser(users.get(random.nextInt(users.size())));
                    comment.setContent("Level 4 Reply to " + parent.getId());
                    comment.setCreatedAt(LocalDateTime.now().minusHours(random.nextInt(24)));
                    comment.setParent(parent);
                    level4.add(comment);
                }
            }

            comments.addAll(level4);

            // Save comments
            commentRepository.saveAll(comments);

            System.out.println("Test article ID: " + testArticleId);
            System.out.println("30 Comments with 4-level tree structure created successfully!");
            System.out.println("50 Users and 50 Articles have been created successfully!");
        };
    }
}
