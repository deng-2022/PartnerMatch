package com.memory.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memory.usercenter.model.entity.User;
import com.memory.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 邓哈哈
 * 2023/4/15 15:22
 * Function: 缓存预热
 * Version 1.0
 */

@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private RedissonClient redissonClient;
    // 重点用户id(提供推送的用户id)
    private final List<Long> mainUserList = List.of(1L);

    // 每天执行，预热推荐用户
    @Scheduled(cron = "0 * * * *  *")   //自己设置时间测试
    public void doCacheRecommendUser() {
        // 设置分布式锁
        RLock lock = redissonClient.getLock("memory:preCacheJob:doCache:lock");

        try {
            // 如果抢锁成功
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                // 遍历每个重点用户
                for (Long userId : mainUserList) {
                    QueryWrapper<User> qw = new QueryWrapper<>();
                    // 分页查询用户信息
                    Page<User> userPage = userService.page(new Page<>(1, 20), qw);
                    // 为每个重点用户设置预查询锁
                    String redisKey = String.format("memory:user:recommend:%s", userId);
                    // 写缓存,30s过期
                    try {
                        redisTemplate.opsForValue().set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.info("error = " + e);
            throw new RuntimeException(e);
        } finally {
            // 如果该分布式锁是自己持有的
            if (lock.isHeldByCurrentThread()) {
                log.info(String.format("unlock: %s", Thread.currentThread().getId()));
                // 释放锁
                lock.unlock();
            }
        }
    }
}
