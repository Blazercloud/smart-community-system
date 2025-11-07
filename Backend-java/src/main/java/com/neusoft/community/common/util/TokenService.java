package com.neusoft.community.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

/**
 * 基于 Redis 的 Token 服务（支持 admin 与 user 双入口）
 *
 * - userToken 以 "auth:user:token:" 开头
 * - adminToken 以 "auth:admin:token:" 开头
 * - 采用 UUID 生成不透明 token，安全性高
 * - TTL 复用 jwt.expiration 配置项
 *
 * @author
 */
@Service
public class TokenService {



    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${jwt.expiration}")
    private Long expirationMillis; // reuse jwt.expiration for Redis token TTL

    private static final String USER_PREFIX = "auth:user:token:";
    private static final String ADMIN_PREFIX = "auth:admin:token:";

    /**
     * 为普通用户生成 token
     */
    public String generateTokenForUser(Long userId) {
        return generateToken(USER_PREFIX, userId);
    }

    /**
     * 为管理员生成 token
     */
    public String generateTokenForAdmin(Long adminId) {
        return generateToken(ADMIN_PREFIX, adminId);
    }

    /**
     * 核心通用生成逻辑
     */
    private String generateToken(String prefix, Long id) {
        String token = UUID.randomUUID().toString().replace("-", "");
        String key = prefix + token;
        redisTemplate.opsForValue().set(key, String.valueOf(id), Duration.ofMillis(expirationMillis));
        return token;
    }

    /**
     * 校验 token 是否存在（无论是 user 还是 admin）
     */
    public boolean validateToken(String token) {
        return getUserIdFromToken(token) != null;
    }

    /**
     * 根据 token 获取对应的用户 ID 或管理员 ID
     */
    public Long getUserIdFromToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        // 优先从 user 空间查
        String val = redisTemplate.opsForValue().get(USER_PREFIX + token);
        if (val != null) {
            return Long.parseLong(val);
        }

        // 再从 admin 空间查
        val = redisTemplate.opsForValue().get(ADMIN_PREFIX + token);
        if (val != null) {
            return Long.parseLong(val);
        }

        return null;
    }

    /**
     * 删除 token（退出登录）
     */
    public void invalidateToken(String token) {
        if (token == null || token.isEmpty()) return;
        redisTemplate.delete(USER_PREFIX + token);
        redisTemplate.delete(ADMIN_PREFIX + token);
    }
}
