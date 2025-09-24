package com.tannguyen.ai.service.inf;

public interface LoginAttemptService {
    boolean isLocked(String username);

    void onAuthSuccess(String username);

    void onAuthFailure(String username);

    void autoUnlockIfWindowExpired(String username);

    long remainingAttempts(String username);
}
