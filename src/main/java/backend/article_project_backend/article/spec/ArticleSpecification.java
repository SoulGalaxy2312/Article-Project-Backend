package backend.article_project_backend.article.spec;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import backend.article_project_backend.article.model.Article;

public class ArticleSpecification {
    
    public static Specification<Article> latestArticles() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            return null;
        };
    }

    public static Specification<Article> hasTopic(String topic) {
        return (root, query, criteriaBuider) -> 
            criteriaBuider.equal(root.get("topic"), topic);
    }

    public static Specification<Article> excludeArticleById(UUID id) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.notEqual(root.get("id"), id);
    }
}
