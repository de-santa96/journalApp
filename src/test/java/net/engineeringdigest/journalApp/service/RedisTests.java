package net.engineeringdigest.journalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    @Disabled
    public void testRedis(){
//        redisTemplate.opsForValue().set("ram", "ram@gmail.com");
//        Object value = redisTemplate.opsForValue().get("ram");

        Object value = redisTemplate.opsForValue().get("salary");

        System.out.println(value);
    }
}
