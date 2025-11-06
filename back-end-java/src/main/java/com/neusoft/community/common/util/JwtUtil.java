package com.neusoft.community.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类，用于生成、解析和验证JWT令牌
 */
@Component
public class JwtUtil {

    /**
     * JWT密钥，从配置文件读取
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT过期时间（毫秒），从配置文件读取
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 获取签名密钥
     * @return SecretKey 签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes;
        try {
            // 尝试使用Base64解码密钥
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (Exception e) {
            // 解码失败则使用UTF-8编码密钥
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        // 如果密钥太短，自动补齐到 32 字节（256位）
        if (keyBytes.length < 32) {
            keyBytes = Arrays.copyOf(keyBytes, 32);
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * 生成JWT令牌
     * @param username 用户名
     * @param role 用户角色
     * @return String 生成的JWT令牌
     */
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        // 在载荷中添加用户角色信息
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)           // 设置载荷
                .subject(username)        // 设置主题（用户名）
                .issuedAt(new Date())     // 设置签发时间
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 设置过期时间
                .signWith(getSigningKey()) // 使用密钥签名
                .compact();               // 生成JWT字符串
    }

    /**
     * 解析JWT令牌
     * @param token JWT字符串
     * @return Claims 解析出的载荷信息
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // 使用密钥验证签名
                .build()
                .parseSignedClaims(token)     // 解析已签名的声明
                .getPayload();                // 获取载荷
    }

    /**
     * 验证 token 是否有效（签名正确且未过期）
     * @param token JWT 字符串
     * @return true 如果 token 有效，false 否则
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            if (claims == null) return false;
            Date expiration = claims.getExpiration();
            // 检查是否过期（过期时间为null或在当前时间之后）
            return expiration == null || expiration.after(new Date());
        } catch (Exception e) {
            // 解析或验证过程中出现异常，token无效
            return false;
        }
    }
}
