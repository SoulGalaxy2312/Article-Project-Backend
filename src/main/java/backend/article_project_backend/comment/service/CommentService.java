package backend.article_project_backend.comment.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import backend.article_project_backend.comment.dto.CommentDTO;
import backend.article_project_backend.comment.mapper.CommentMapper;
import backend.article_project_backend.comment.model.Comment;
import backend.article_project_backend.comment.repository.CommentRepository;

@Service
public class CommentService {
    
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentDTO> getArticleComments(String articleId) {
        UUID uuid = UUID.fromString(articleId);
        List<Comment> comments = commentRepository.findByArticleId(uuid);

        return comments.stream()
                        .filter(c -> c.getParent() == null)
                        .map(c -> CommentMapper.toCommentDTO(c, comments))
                        .collect(Collectors.toList());
    }
}
