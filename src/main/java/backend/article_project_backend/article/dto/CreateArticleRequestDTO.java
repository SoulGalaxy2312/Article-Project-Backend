package backend.article_project_backend.article.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateArticleRequestDTO(

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title, 

    @NotBlank(message = "Topic is required")
    String topic,

    @NotNull(message = "Main image is required")
    MultipartFile mainImage,

    List<String> tags,

    @NotBlank(message = "Abstract content is required")
    String abstractContent,
    
    boolean isPremium
) {
    
}
