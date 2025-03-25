package backend.article_project_backend.topic.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import backend.article_project_backend.topic.model.Topic;
import backend.article_project_backend.topic.service.TopicService;
import backend.article_project_backend.utils.common.dto.ApiResponse;
import backend.article_project_backend.utils.common.path.AppPaths;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping(AppPaths.TOPIC_URI)
@Slf4j
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }
    
    @GetMapping("/explore-topics")
    public ResponseEntity<ApiResponse<List<Topic>>> getAllTopics() {
        log.info("Fetching topics");
        List<Topic> topics = topicService.getAllTopics();
        ApiResponse<List<Topic>> response = new ApiResponse<List<Topic>>(topics);
        return ResponseEntity.ok().body(response);
    }
    
}
