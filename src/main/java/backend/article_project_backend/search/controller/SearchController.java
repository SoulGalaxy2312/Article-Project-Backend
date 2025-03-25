package backend.article_project_backend.search.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import backend.article_project_backend.article.dto.ArticlePagingDTO;
import backend.article_project_backend.search.dto.SearchRequestDTO;
import backend.article_project_backend.search.service.SearchService;
import backend.article_project_backend.utils.common.dto.ApiResponse;
import backend.article_project_backend.utils.common.path.AppPaths;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@RestController
@RequestMapping(AppPaths.SEARCH_URI)
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService ) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ArticlePagingDTO>> search(
        @ModelAttribute SearchRequestDTO searchRequestDTO,
        @RequestParam(defaultValue = "0") int pageNumber) {

            ArticlePagingDTO result = null;

            if (searchRequestDTO.topicId() != null) {
                result = searchService.searchByTopic(searchRequestDTO.topicId(), pageNumber);
            } else if (searchRequestDTO.tag() != null) {
                result = searchService.searchByTag(searchRequestDTO.tag(), pageNumber);
            } else if (searchRequestDTO.keywords() != null) {
                result = searchService.fullTextSearch(searchRequestDTO.keywords(), pageNumber);
            }

            if (result == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<ArticlePagingDTO>(null));
            }

            return ResponseEntity.ok().body(new ApiResponse<ArticlePagingDTO>(result));
    }
}
