package com.tannguyen.ai.service.inf;

public interface TokenBlacklistService {
    void blacklistToken(String token);

    boolean isTokenBlacklisted(String token);
}
