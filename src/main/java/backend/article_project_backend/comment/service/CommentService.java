package backend.article_project_backend.comment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import backend.article_project_backend.article.model.Article;
import backend.article_project_backend.article.repository.ArticleRepository;
import backend.article_project_backend.comment.dto.CommentDTO;
import backend.article_project_backend.comment.dto.CreateCommentRequestDTO;
import backend.article_project_backend.comment.dto.CreateCommentResponseDTO;
import backend.article_project_backend.comment.mapper.CommentMapper;
import backend.article_project_backend.comment.model.Comment;
import backend.article_project_backend.comment.repository.CommentRepository;
import backend.article_project_backend.user.model.User;
import backend.article_project_backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public List<CommentDTO> getArticleComments(UUID articleId) {
        List<Comment> comments = commentRepository.findByArticleId(articleId);

        return comments.stream()
                        .filter(c -> c.getParent() == null)
                        .map(c -> CommentMapper.toCommentDTO(c, comments))
                        .collect(Collectors.toList());
    }

    public CreateCommentResponseDTO createComment(UUID articleId, CreateCommentRequestDTO createCommentRequestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new EntityNotFoundException("Not found article with id: " + articleId));

        User user = 
            userRepository
                .findByUsername(username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Not found user with username: " + username));
        
        UUID parentCommentId = createCommentRequestDTO.parentCommentId();
        Comment parentComment = null;
        if (parentCommentId != null) {
            parentComment = 
                commentRepository
                    .findById(parentCommentId)
                    .orElseThrow(() -> new EntityNotFoundException("Not found comment with id: " + parentCommentId));
        }

        Comment savedComment = commentRepository.save(new Comment(
            article,
            user,
            createCommentRequestDTO.content(),
            LocalDateTime.now(),
            parentComment
        ));

        return new CreateCommentResponseDTO(true, "Save comment", savedComment.getId(), savedComment.getCreatedAt());
    }
}

