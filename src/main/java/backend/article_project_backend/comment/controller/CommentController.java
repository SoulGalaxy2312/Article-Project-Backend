package backend.article_project_backend.comment.controller;

import backend.article_project_backend.comment.dto.CommentDTO;
import backend.article_project_backend.comment.dto.CreateCommentRequestDTO;
import backend.article_project_backend.comment.model.Comment;
import backend.article_project_backend.comment.service.CommentService;
import backend.article_project_backend.utils.common.dto.ApiResponse;
import backend.article_project_backend.utils.common.path.AppPaths;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@RequestMapping(AppPaths.ARTICLE_URI)
public class CommentController {
    
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getArticleComments(@PathVariable UUID articleId) {
        log.info("Get comments of article with id {}", articleId);
        List<CommentDTO> comments = commentService.getArticleComments(articleId);
        ApiResponse<List<CommentDTO>> response = new ApiResponse<List<CommentDTO>>(comments);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{articleId}/createComment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(
        @PathVariable UUID articleId,
        @RequestBody CreateCommentRequestDTO createCommentRequestDTO) {

            log.info("Create comment for article with id {}", articleId);
            CommentDTO responseDTO = commentService.createComment(articleId, createCommentRequestDTO);
            ApiResponse<CommentDTO> response = new ApiResponse<CommentDTO>(responseDTO);
            return ResponseEntity.ok().body(response);
    }
    
}
