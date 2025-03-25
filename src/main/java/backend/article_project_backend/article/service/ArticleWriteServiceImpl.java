package backend.article_project_backend.article.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import backend.article_project_backend.article.dto.CreateArticleRequestDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.mapper.ArticleMapper;
import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.model.ArticleStatusEnum;
import backend.article_project_backend.article.repository.ArticleRepository;
import backend.article_project_backend.article.spec.ArticleSpecification;
import backend.article_project_backend.image.service.ImageService;
import backend.article_project_backend.topic.model.Topic;
import backend.article_project_backend.topic.service.TopicService;
import backend.article_project_backend.user.model.User;
import backend.article_project_backend.utils.config.cache.RedisService;
import backend.article_project_backend.utils.security.authentication.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import backend.article_project_backend.utils.common.cache.RedisKeys;

@Service
@Primary
@Slf4j
public class ArticleWriteServiceImpl implements ArticleWriteService{

    
    public final ArticleRepository articleRepository;
    public final ImageService imageService;
    public final TopicService topicService;
    public final RedisService redisService;

    public ArticleWriteServiceImpl(ArticleRepository articleRepository, ImageService imageService, TopicService topicService, RedisService redisService) {
        this.articleRepository = articleRepository;
        this.imageService = imageService;
        this.topicService = topicService;
        this.redisService = redisService;
    }

    @Override
    public FullArticleDTO createArticle(CreateArticleRequestDTO createArticleRequestDTO) {
        Article article = new Article();
        try {
            String mainImageURL = imageService.uploadImage(createArticleRequestDTO.mainImage());
            article.setMainImageUrl(mainImageURL);    
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userPrincipal.getUser();            
            article.setUser(user);
        } else {
            throw new RuntimeException("User not authenticated");
        }
        
        article.setTitle(createArticleRequestDTO.title());
        
        Topic savedTopic = topicService.saveTopic(createArticleRequestDTO.topic());
        article.setTopic(savedTopic);
        
        article.setTags(createArticleRequestDTO.tags());
        article.setAbstractContent(createArticleRequestDTO.abstractContent());
        article.setPremium(createArticleRequestDTO.isPremium());
        article.setCreatedAt(null);
        article.setStatus(ArticleStatusEnum.PENDING);
        article.setViews(0);
        article.setContent(createArticleRequestDTO.content());
        article.setCreatedAt(LocalDateTime.now());
        articleRepository.save(article);

        return ArticleMapper.toFullArticleDTO(article);
    }

    public String changeStatus(UUID articleId, ArticleStatusEnum status) {
        log.info("Service layer: change article status");
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new EntityNotFoundException("Not found article with id: " + articleId));
    
        article.setStatus(status);
        articleRepository.save(article);

        if (ArticleStatusEnum.PUBLISHED.equals(status)) {
            Specification<Article> spec = Specification.where(ArticleSpecification.withStatus(ArticleStatusEnum.PUBLISHED));
            List<Article> articles = articleRepository.findAll(spec);
            int size = articles.size();
            redisService.deleteData(RedisKeys.TOTAL_PUBLISHED_ARTICLES);
            redisService.saveData(RedisKeys.TOTAL_PUBLISHED_ARTICLES, size);
        }
    
        return "Article status updated to " + status.name();
    }
}
