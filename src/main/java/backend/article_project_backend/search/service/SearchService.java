package backend.article_project_backend.search.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import backend.article_project_backend.article.dto.ArticlePagingDTO;
import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.model.ArticleStatusEnum;
import backend.article_project_backend.article.service.ArticleReadService;
import backend.article_project_backend.article.spec.ArticleSpecification;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SearchService {
    
    private final ArticleReadService articleReadService;

    private final int PAGE_SIZE = 10;

    public SearchService(ArticleReadService articleReadService) {
        this.articleReadService = articleReadService;
    }

    public ArticlePagingDTO search(Specification<Article> spec, int pageNumber) {
        log.info("Searching...");
        Specification<Article> finalSpec = Specification.where(spec)
                                                    .and(ArticleSpecification.withStatus(ArticleStatusEnum.PUBLISHED))
                                                    .and(ArticleSpecification.latestArticles());
        return articleReadService.fetchArticles(finalSpec, pageNumber, PAGE_SIZE, false);
    }

    public ArticlePagingDTO searchByTopic(Long topicId, int pageNumber) {
        log.info("Searching: Search by topic");
        return search(ArticleSpecification.hasTopic(topicId), pageNumber);
    }

    public ArticlePagingDTO searchByTag(String tag, int pageNumber) {
        log.info("Searching: Search by tag");
        log.info("Tag: {}", tag);
        log.info("Page number: {}", pageNumber);
        return search(ArticleSpecification.hasTag(tag), pageNumber);
    }

    public ArticlePagingDTO fullTextSearch(String keywords, int pageNumber) {
        log.info("Searching: Search by tag");
        return search(ArticleSpecification.fullTextSearch(keywords), pageNumber);
    }
}
