package backend.article_project_backend.article.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.dto.FullArticleDTO;
import backend.article_project_backend.article.mapper.ArticleMapper;
import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ArticleService {
    
    private final ArticleRepository articleRepository;

    private final int HOME_PAGE_NUM_ARTICLES = 25;
    private final int HOME_PAGE_NUM_MOST_VIEWS_ARTICLES = 10;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticlePreviewDTO> getArticlesPreviewByPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, HOME_PAGE_NUM_ARTICLES);
        
        Page<Article> articles = articleRepository.findAllByOrderByCreatedAtDesc(pageable);

        return articles.stream()
                        .map(ArticleMapper::toArticlePreviewDTO)
                        .collect(Collectors.toList());
    }

    public List<ArticlePreviewDTO> getTenArticlesWithMostViews() {
        Pageable pageable = PageRequest.ofSize(HOME_PAGE_NUM_MOST_VIEWS_ARTICLES);

        Page<Article> articles = articleRepository.findAllByOrderByViewsDesc(pageable);

        return articles.stream()
                        .map(ArticleMapper::toArticlePreviewDTO)
                        .collect(Collectors.toList());
    }

    public FullArticleDTO getSpecificArticle(String id) {
        UUID uuid = UUID.fromString(id);

        return articleRepository
                    .findById(uuid)
                    .map(ArticleMapper::toFullArticleDTO)
                    .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
    }
}
