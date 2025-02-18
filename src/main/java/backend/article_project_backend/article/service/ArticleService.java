package backend.article_project_backend.article.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import backend.article_project_backend.article.dto.ArticlePreviewDTO;
import backend.article_project_backend.article.mapper.ArticleMapper;
import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.repository.ArticleRepository;

@Service
public class ArticleService {
    
    private final ArticleRepository articleRepository;

    private final int HOME_PAGE_NUM_ARTICLE = 25;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticlePreviewDTO> getArticlesPreviewByPage(int pageNumber) {
        Pageable pageable = PageRequest.of(
            pageNumber, 
            HOME_PAGE_NUM_ARTICLE,
            Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<Article> articles = articleRepository.findAllByOrderByCreatedAtDesc(pageable);

        return articles.stream()
                        .map(ArticleMapper::toDTO)
                        .collect(Collectors.toList());
    }
}
