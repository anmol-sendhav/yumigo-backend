package com.yumigo.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;
import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CartStore {

    private static final Duration TTL = Duration.ofDays(3);

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    private String key(Long userId) {
        return "cart:" + userId;
    }

    public Optional<CartData> find(Long userId) {
        String json = redis.opsForValue().get(key(userId));
        if (json == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, CartData.class));
        } catch (JacksonException e) {
            throw new IllegalStateException("Stored cart is unreadable", e);
        }
    }

    public void save(Long userId, CartData cart) {
        try {
            redis.opsForValue().set(key(userId), objectMapper.writeValueAsString(cart), TTL);
        } catch (JacksonException e) {
            throw new IllegalStateException("Cart could not be saved", e);
        }
    }
    public void delete(Long userId) {
        redis.delete(key(userId));
    }
}