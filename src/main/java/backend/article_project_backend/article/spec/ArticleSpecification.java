package backend.article_project_backend.article.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.model.ArticleStatusEnum;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class ArticleSpecification {
    
    public static Specification<Article> latestArticles() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            return null;
        };
    }

    public static Specification<Article> hasTopic(Long topicId) {
        return (root, query, criteriaBuider) -> {
            if (topicId == null) {
                return criteriaBuider.conjunction();
            } 
            return criteriaBuider.equal(root.get("topic").get("id"), topicId);
        };  
    }

    public static Specification<Article> excludeArticleById(UUID id) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.notEqual(root.get("id"), id);
    }

    public static Specification<Article> withStatus(ArticleStatusEnum status) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Article> getArticleWithUserId(Integer userId) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Article> hasTag(String tag) {
        return (root, query, criteriaBuilder) -> {
            Join<Article, String> tagsJoin = root.join("tags");
            return criteriaBuilder.equal(tagsJoin, tag);
        };
    }

    public static Specification<Article> fullTextSearch(String keywords) {
        return (root, query, criteriaBuilder) -> {
            if (keywords == null || keywords.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + keywords.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("abstractContent")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), pattern));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
