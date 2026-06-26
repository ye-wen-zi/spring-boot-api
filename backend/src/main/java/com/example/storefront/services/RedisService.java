package com.example.storefront.services;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public <V> void put(String key, V value, long timeout, TimeUnit timeUnit) {
        try {
            this.redisTemplate.opsForValue().set(key, this.objectMapper.writeValueAsString(value), timeout, timeUnit);
        } catch (JsonProcessingException e) {
            log.error("{}", e);
        }
    }

    public <V> V get(String key, Class<V> clazz) {
        String jsonString = this.redisTemplate.opsForValue().get(key);
        if (jsonString == null)
            return null;
        try {
            return this.objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            log.error("{}", e);
            return null;
        }
    }

    public void delete(String key) {
        this.redisTemplate.delete(key);
    }

}
