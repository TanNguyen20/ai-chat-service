package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.service.inf.TokenBlacklistService;
import com.tannguyen.ai.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private JwtUtil jwtUtil;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void blacklistToken(String token) {
        long expiration = jwtUtil.getExpirationFromToken(token);
        if (expiration > 0) {
            redisTemplate.opsForValue().set(token, "blacklisted", expiration, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }

}
