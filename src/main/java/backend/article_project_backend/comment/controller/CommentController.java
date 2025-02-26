package backend.article_project_backend.comment.controller;

import backend.article_project_backend.comment.dto.CommentDTO;
import backend.article_project_backend.comment.service.CommentService;
import backend.article_project_backend.utils.common.path.AppPaths;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(AppPaths.ARTICLE_URI)
public class CommentController {
    
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{articleId}/comments")
    public List<CommentDTO> getArticleComments(@PathVariable String articleId) {
        return commentService.getArticleComments(articleId);
    }
}
