package com.neusoft.community.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

/**
 * 基于 Redis 的 Token 服务（不透明 token）
 */
@Service
public class TokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${jwt.expiration}")
    private Long expirationMillis; // reuse jwt.expiration for TTL

    private static final String KEY_PREFIX = "auth:token:";

    public String generateTokenForUser(Long userId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        String key = KEY_PREFIX + token;
        redisTemplate.opsForValue().set(key, String.valueOf(userId), Duration.ofMillis(expirationMillis));
        return token;
    }

    public Long getUserIdForToken(String token) {
        if (token == null) return null;
        String key = KEY_PREFIX + token;
        String val = redisTemplate.opsForValue().get(key);
        if (val == null) return null;
        try {
            return Long.parseLong(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        return getUserIdForToken(token) != null;
    }

    public void invalidateToken(String token) {
        if (token == null) return;
        String key = KEY_PREFIX + token;
        redisTemplate.delete(key);
    }
}
