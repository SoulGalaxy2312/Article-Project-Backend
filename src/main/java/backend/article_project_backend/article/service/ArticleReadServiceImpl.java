package backend.article_project_backend.article.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import backend.article_project_backend.article.dto.ArticlePagingDTO;
import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.dto.ArticleProfileDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.mapper.ArticleMapper;
import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.model.ArticleStatusEnum;
import backend.article_project_backend.article.repository.ArticleRepository;
import backend.article_project_backend.article.spec.ArticleSpecification;
import backend.article_project_backend.user.model.User;
import backend.article_project_backend.user.model.UserRole;
import backend.article_project_backend.utils.config.cache.RedisService;
import backend.article_project_backend.utils.security.authentication.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import backend.article_project_backend.utils.common.cache.RedisKeys;

@Service
@Primary
@Slf4j
public class ArticleReadServiceImpl implements ArticleReadService {
    
    private final ArticleRepository articleRepository;
    private final RedisService redisService;

    private final int HOMEPAGE_NUM_LATEST_ARTICLES = 10;
    private final int HOMEPAGE_NUM_MOST_VIEWS_ARTICLES = 3;
    private final int NUM_ARTICLE_RELEVANT = 5;

    public ArticleReadServiceImpl(ArticleRepository articleRepository, RedisService redisService) {
        this.articleRepository = articleRepository;
        this.redisService = redisService;
    }

    public ArticlePagingDTO fetchArticles(
        Specification<Article> spec, 
        int pageNumber, 
        int pageSize,
        boolean isHomepageRequest) {
            Pageable page = PageRequest.of(pageNumber, pageSize);
            Page<Article> articles = articleRepository.findAll(spec, page);
            List<ArticlePreviewDTO> articleDTOs = ArticleMapper.toArticlePreviewDTOsList(articles.getContent());

            int totalArticles = isHomepageRequest ? getTotalPublishedArticles(articles) : articles.getNumberOfElements();
            int totalPages = (int) Math.ceil((double) totalArticles / pageSize);

            return new ArticlePagingDTO(articleDTOs, pageNumber < totalPages - 1);
    }

    private int getTotalPublishedArticles(Page<Article> articles) {
        Object totalArticlesObj = redisService.getData(RedisKeys.TOTAL_PUBLISHED_ARTICLES);
        if (totalArticlesObj == null) {
            log.info("No published article");
            return 0;
        }
        log.info("Total articles from cache: {}", totalArticlesObj.toString());
        if (totalArticlesObj instanceof Integer) {
            return (Integer) totalArticlesObj;
        } 

        int total = (int) articles.getNumberOfElements();
        redisService.deleteData(RedisKeys.TOTAL_PUBLISHED_ARTICLES);
        redisService.saveData(RedisKeys.TOTAL_PUBLISHED_ARTICLES, total);
        return total;
    }

    @Override
    public ArticlePagingDTO getHomepageLatestArticles(int pageNumber) {
        log.info("Fetching latest article for homepage");
        Specification<Article> spec = Specification.where(ArticleSpecification.latestArticles())
                                                .and(ArticleSpecification.withStatus(ArticleStatusEnum.PUBLISHED));
        return fetchArticles(spec, pageNumber, HOMEPAGE_NUM_LATEST_ARTICLES, true);
    }


    @Override
    public List<ArticlePreviewDTO> getHomepageMostViewedArticles() {
        Pageable pageable = PageRequest.of(0, HOMEPAGE_NUM_MOST_VIEWS_ARTICLES, Sort.by(Sort.Direction.DESC, "views"));
        Specification<Article> spec = Specification.where(ArticleSpecification.withStatus(ArticleStatusEnum.PUBLISHED));

        Page<Article> articles = articleRepository.findAll(spec, pageable);
        return ArticleMapper.toArticlePreviewDTOsList(articles.getContent());
    }

    @Override
    public FullArticleDTO getSpecificArticle(UUID id) {
        log.info("Service layer gets to work with id {}", id);
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
        
        if (article.getStatus() != ArticleStatusEnum.PUBLISHED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this article.");
        }

        if (article.isPremium()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must have login to access this premium article.");
            }
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            String redisKey = RedisKeys.USER_ROLE + username;

            log.info("Redis key: " + redisKey);
            Object curUserRoleObj = redisService.getData(redisKey);
            if (curUserRoleObj == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must have login to access this premium article.");
            }
            String curUserRole = redisService.getData(redisKey).toString();
            boolean hasRoleUser = UserRole.ROLE_USER.toString().equals(curUserRole);
            if (!hasRoleUser) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must have the USER role to access the premium article.");
            }
        } 

        article.setViews(article.getViews() + 1);
        return ArticleMapper.toFullArticleDTO(article);
    }

    @Override
    public List<ArticlePreviewDTO> getRelevantArticle(UUID id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Not found article with id: " + id));
        
        Long topicId = article.getTopic().getId();
        Specification<Article> spec = Specification.where(ArticleSpecification.hasTopic(topicId))
                                                    .and(ArticleSpecification.excludeArticleById(id))
                                                    .and(ArticleSpecification.latestArticles());
        Pageable pageable = PageRequest.ofSize(NUM_ARTICLE_RELEVANT);


        List<Article> relevantArticles = articleRepository.findAll(spec, pageable).getContent();
        return relevantArticles.stream()
                                .map(a -> ArticleMapper.toArticlePreviewDTO(a))
                                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public List<ArticleProfileDTO> getArticleInProfiles(ArticleStatusEnum articleStatus) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object userObject = authentication.getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal) userObject;
        User user = userPrincipal.getUser();

        Specification<Article> spec = Specification.where(ArticleSpecification.withStatus(articleStatus));
        if (UserRole.ROLE_USER.equals(user.getRole())) {
            log.info("USER gets authenticated");
            spec = spec.and(ArticleSpecification.getArticleWithUserId(user.getId()));
        }

        List<Article> articles = articleRepository.findAll(spec);
        return ArticleMapper.toArticleProfileDTOsList(articles);
    }
}
