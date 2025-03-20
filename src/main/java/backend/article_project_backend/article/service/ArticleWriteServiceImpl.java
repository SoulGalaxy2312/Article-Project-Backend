package backend.article_project_backend.article.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import backend.article_project_backend.article.dto.CreateArticleRequestDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.mapper.ArticleMapper;
import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.model.ArticleStatusEnum;
import backend.article_project_backend.article.repository.ArticleRepository;
import backend.article_project_backend.image.service.ImageService;
import backend.article_project_backend.topic.model.Topic;
import backend.article_project_backend.topic.service.TopicService;
import backend.article_project_backend.user.model.User;
import backend.article_project_backend.utils.security.authentication.UserPrincipal;

@Service
@Primary
public class ArticleWriteServiceImpl implements ArticleWriteService{
    
    public final ArticleRepository articleRepository;
    public final ImageService imageService;
    public final TopicService topicService;

    public ArticleWriteServiceImpl(ArticleRepository articleRepository, ImageService imageService, TopicService topicService) {
        this.articleRepository = articleRepository;
        this.imageService = imageService;
        this.topicService = topicService;
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
        articleRepository.save(article);

        return ArticleMapper.toFullArticleDTO(article);
    }
}
