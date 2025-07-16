package com.nicoletti.rinharouter.service.impl;

import com.nicoletti.rinharouter.dto.ServiceStatusDTO;
import com.nicoletti.rinharouter.service.api.ServiceStatusCacheReactive;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ServiceStatusCacheReactiveImpl implements ServiceStatusCacheReactive {

    private static final String PAYMENT_SERVICE_KEY = "PAYMENT:SERVICE";
    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    public ServiceStatusCacheReactiveImpl(ReactiveRedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Boolean> setStatus(ServiceStatusDTO dto) {
        return redisTemplate.opsForValue()
                .set(PAYMENT_SERVICE_KEY, dto, Duration.ofSeconds(5));
    }

    @Override
    public Mono<ServiceStatusDTO> getStatus() {
        return redisTemplate.opsForValue()
                .get(PAYMENT_SERVICE_KEY)
                .cast(ServiceStatusDTO.class);
    }
}
