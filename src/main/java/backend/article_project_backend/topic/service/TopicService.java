package backend.article_project_backend.topic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import backend.article_project_backend.topic.model.Topic;
import backend.article_project_backend.topic.repository.TopicRepository;
import backend.article_project_backend.utils.config.cache.RedisService;
import lombok.extern.slf4j.Slf4j;
import backend.article_project_backend.utils.common.cache.RedisKeys;

@Service
@Slf4j
public class TopicService {
    
    private final RedisService redisService;
    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository, RedisService redisService) {
        this.topicRepository = topicRepository;
        this.redisService = redisService;
    }
    
    public Topic saveTopic(String topicName) {
        log.info("Saving topic {}", topicName);
        Optional<Topic> existingTopic = topicRepository.findByName(topicName);
        if (existingTopic.isPresent()) {
            log.info("{} already exists", topicName);
            return existingTopic.get();
        }

        log.info("Saving topic {} to the repository", topicName);
        Topic savedTopic = new Topic(null, topicName);
        savedTopic = topicRepository.save(savedTopic);

        List<Topic> topics = topicRepository.findAll();
        redisService.deleteData(RedisKeys.ALL_TOPICS);
        redisService.saveData(RedisKeys.ALL_TOPICS, topics);

        return savedTopic;
    }

    public List<Topic> getAllTopics() {
        log.info("Getting all topics");
        Object topicsObj = redisService.getData(RedisKeys.ALL_TOPICS);
        if (topicsObj != null && topicsObj instanceof List) {
            log.info("Topics are stored in cache");
            return (List<Topic>) topicsObj;
        }

        log.info("Topics are not in cache. Getting topics from  repository");
        List<Topic> topics = topicRepository.findAll();
        redisService.saveData(RedisKeys.ALL_TOPICS, topics);
        return topics;
    }
}
