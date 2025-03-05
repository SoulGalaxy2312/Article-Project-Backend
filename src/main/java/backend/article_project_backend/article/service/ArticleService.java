package backend.article_project_backend.article.service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.article_project_backend.article.controller.ArticleController;
import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.mapper.ArticleMapper;
import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.repository.ArticleRepository;
import backend.article_project_backend.article.spec.ArticleSpecification;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ArticleService {
    
    private final ArticleRepository articleRepository;

    private final int HOMEPAGE_NUM_LATEST_ARTICLES = 10;
    private final int HOMEPAGE_NUM_MOST_VIEWS_ARTICLES = 3;

    private final Logger logger = Logger.getLogger(ArticleController.class.getName());

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticlePreviewDTO> getHomepageLatestArticles(int pageNumber) {
        logger.info("Get into Article service");
        Specification<Article> spec = Specification.where(ArticleSpecification.latestArticles());
        Pageable pageable = PageRequest.of(pageNumber, HOMEPAGE_NUM_LATEST_ARTICLES);        
        Page<Article> articles = articleRepository.findAll(spec, pageable);
        return ArticleMapper.toArticlePreviewDTOsList(articles.getContent());
    }

    public List<ArticlePreviewDTO> getHomepageMostViewedArticles() {
        Pageable pageable = PageRequest.ofSize(HOMEPAGE_NUM_MOST_VIEWS_ARTICLES);
        Page<Article> articles = articleRepository.findAllByOrderByViewsDesc(pageable);
        return ArticleMapper.toArticlePreviewDTOsList(articles.getContent());
    }

    public FullArticleDTO getSpecificArticle(UUID id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
        
        if (article.isPremium()) {
            if (!hasRoleUser()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must have the USER role to access the premium article.");
            }
        }

        return ArticleMapper.toFullArticleDTO(article);
    }

    private boolean hasRoleUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public List<ArticlePreviewDTO> getRelevantArticle(UUID id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Not found article with id: " + id));
        
        String topic = article.getTopic();
        Specification<Article> spec = Specification.where(ArticleSpecification.hasTopic(topic))
                                                    .and(ArticleSpecification.excludeArticleById(id))
                                                    .and(ArticleSpecification.latestArticles());
        Pageable pageable = PageRequest.ofSize(5);

        List<Article> relevantArticles = articleRepository.findAll(spec, pageable).getContent();
        return relevantArticles.stream()
                                .map(a -> ArticleMapper.toArticlePreviewDTO(a))
                                .collect(Collectors.toList());
    }
}
