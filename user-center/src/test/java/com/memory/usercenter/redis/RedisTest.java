package com.memory.usercenter.redis;

import com.memory.usercenter.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author 邓哈哈
 * 2023/4/13 20:54
 * Function:
 * Version 1.0
 */
@SpringBootTest
@Slf4j
public class RedisTest {
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();

    @Test
    public void test1() {
        stringRedisTemplate.opsForValue().set("memory", "邓哈哈");
//        stringRedisTemplate.opsForValue().set("memory", 18);
        redisTemplate.opsForValue().set("memory2", 9);
        String memory = stringRedisTemplate.opsForValue().get("memory");
    }

    @Test
    public void test2() {
        // 增
        redisTemplate.opsForValue().set("memoryString", "dog");
        redisTemplate.opsForValue().set("memoryInt", 1);
        redisTemplate.opsForValue().set("memoryDouble", 3.0);
        User user = new User();
        user.setId(9999L);
        user.setUserAccount("memoryc7b93cb1b3");
        redisTemplate.opsForValue().set("memoryUser", user);
        // 查
        Object dog = redisTemplate.opsForValue().get("memoryString");
        Assertions.assertTrue("dog".equals(dog));
        Object anInt = redisTemplate.opsForValue().get("memoryInt");
        Assertions.assertTrue(1 == (Integer) anInt);
        Object anDouble = redisTemplate.opsForValue().get("memoryDouble");
        Assertions.assertTrue(3.0 == (Double) anDouble);
        Object memoryUser = redisTemplate.opsForValue().get("memoryUser");
        log.info(memoryUser + "");
        // 删
        redisTemplate.delete("memoryInt");
    }
}
