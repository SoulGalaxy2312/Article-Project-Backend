package backend.article_project_backend.comment.controller;

import backend.article_project_backend.comment.dto.CommentDTO;
import backend.article_project_backend.comment.dto.CreateCommentRequestDTO;
import backend.article_project_backend.comment.dto.CreateCommentResponseDTO;
import backend.article_project_backend.comment.service.CommentService;
import backend.article_project_backend.utils.common.path.AppPaths;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(AppPaths.ARTICLE_URI)
public class CommentController {
    
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{articleId}/comments")
    public List<CommentDTO> getArticleComments(@PathVariable UUID articleId) {
        return commentService.getArticleComments(articleId);
    }

    @PostMapping("/{articleId}/createComment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CreateCommentResponseDTO> createComment(
        @PathVariable UUID articleId,
        @RequestBody CreateCommentRequestDTO createCommentRequestDTO) {

        CreateCommentResponseDTO responseDTO = commentService.createComment(articleId, createCommentRequestDTO);
        
        return ResponseEntity.ok().body(responseDTO);
    }
    
}
