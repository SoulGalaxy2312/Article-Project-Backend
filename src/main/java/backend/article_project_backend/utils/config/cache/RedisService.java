package backend.article_project_backend.utils.config.cache;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Collections;

@Service
public class RedisService {
    
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveData(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public void saveList(String key, List<String> list) {
        redisTemplate.opsForValue().set(key, list);
    }

    public List<String> getList(String key) {
        Object data = redisTemplate.opsForValue().get(key);
        if (data instanceof List<?>) {
            return ((List<?>) data).stream()
                                    .filter(String.class::isInstance)  
                                    .map(String.class::cast)          
                                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
