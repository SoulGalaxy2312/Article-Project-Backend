package backend.article_project_backend.comment.mapper;

import java.util.List;
import java.util.stream.Collectors;

import backend.article_project_backend.comment.dto.CommentDTO;
import backend.article_project_backend.comment.model.Comment;
import backend.article_project_backend.user.mapper.UserMapper;

public class CommentMapper {
    
    public static CommentDTO toCommentDTO(Comment comment, List<Comment> allComments) {
        return new CommentDTO(
            comment.getId(), 
            UserMapper.toUserDTO(comment.getUser()),
            comment.getContent(), 
            comment.getCreatedAt(),
            allComments.stream()
                    .filter(c -> c.getParent() != null && c.getParent().getId().equals(comment.getId()))
                    .map(c -> CommentMapper.toCommentDTO(c, allComments))
                    .collect(Collectors.toList())
        );
    }
}
