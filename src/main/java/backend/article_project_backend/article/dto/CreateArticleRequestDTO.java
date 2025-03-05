package backend.article_project_backend.article.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record CreateArticleRequestDTO(
    String title,
    String topic,
    MultipartFile mainImage,
    List<String> tags,
    String abstractContent,
    boolean isPremium
) {
    
}
