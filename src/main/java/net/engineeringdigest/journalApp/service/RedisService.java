package net.engineeringdigest.journalApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {
    @Autowired
    RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> entityClass){
        Object o = redisTemplate.opsForValue().get(key);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            T t = objectMapper.readValue(o.toString(), entityClass);
            return t;
        } catch (Exception e) {
            log.error("Error while fetching from redis ", e);
            return null;
        }
    }

    public void set(String key, Object value, long ttl){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String s = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, s, ttl, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("exception while setting value", e);
        }
    }
}
