package backend.article_project_backend.search.dto;

public record SearchRequestDTO(
    String keywords,
    Long topicId,
    String tag
) {
}
