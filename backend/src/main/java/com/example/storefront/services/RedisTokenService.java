package com.example.storefront.services;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTokenService {
    private final StringRedisTemplate stringRedisTemplate;

    public RedisTokenService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

     public String creatKey(String username) {
        return "refresh_token:" + username;
    }

    public void saveRefreshToken(String username, String token, long duration, TimeUnit timeUnit) {
        this.stringRedisTemplate.opsForValue().set(this.creatKey(username), token, duration, timeUnit);
    }

    public String getRefreshToken(String username) {
        return this.stringRedisTemplate.opsForValue().get(this.creatKey(username));
    }

    public void deleteRefreshToken(String username) {
        this.stringRedisTemplate.delete(this.creatKey(username));
    }
}
