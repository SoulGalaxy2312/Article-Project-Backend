package backend.article_project_backend.article.dto;

import java.util.List;

public record ArticlePagingDTO(
    List<? extends Object> articles,
    boolean hasMore
) {
}