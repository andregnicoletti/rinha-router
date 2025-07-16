package com.nicoletti.rinharouter.service.impl;

import com.nicoletti.rinharouter.dto.ServiceStatusDTO;
import com.nicoletti.rinharouter.service.api.ServiceStatusCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ServiceStatusCacheImpl implements ServiceStatusCache {

    private static final String PAYMENT_SERVICE_KEY = "PAYMENT:SERVICE";
    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration TTL = Duration.ofSeconds(5);

    @Autowired
    private Environment environment;

    public ServiceStatusCacheImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setStatus(ServiceStatusDTO dto) {
        redisTemplate.opsForValue().set(PAYMENT_SERVICE_KEY, dto, TTL);
    }

    @Override
    public ServiceStatusDTO getStatus() {
        return (ServiceStatusDTO) redisTemplate.opsForValue().get(PAYMENT_SERVICE_KEY);
    }

}
