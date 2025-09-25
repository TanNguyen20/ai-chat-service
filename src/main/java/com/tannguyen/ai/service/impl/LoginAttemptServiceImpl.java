package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.model.primary.User;
import com.tannguyen.ai.repository.primary.UserRepository;
import com.tannguyen.ai.service.inf.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private static final String ATTEMPTS_KEY_FMT = "auth:attempts:%s"; // TTL 30m
    private static final String LOCK_KEY_FMT     = "auth:lock:%s";     // TTL 30m
    private static final long   LOCK_AFTER       = 5L;
    private static final Duration WINDOW         = Duration.ofMinutes(30);

    private final StringRedisTemplate redis;
    private final UserRepository userRepository;

    public boolean isLocked(String username) {
        String lockKey = lockKey(username);
        Boolean has = redis.hasKey(lockKey);
        return Boolean.TRUE.equals(has);
    }

    /** Called when a login attempt fails (BadCredentials). */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onAuthFailure(String username) {
        String attemptsKey = attemptsKey(username);
        Long attempts = redis.opsForValue().increment(attemptsKey); // atomic
        if (attempts != null && attempts == 1L) {
            redis.expire(attemptsKey, WINDOW); // start 30m window on first failure
        }
        if (attempts != null && attempts >= LOCK_AFTER) {
            // Set a lock key (with 30m TTL)
            String lockKey = lockKey(username);
            redis.opsForValue().set(lockKey, "1", WINDOW);

            // Update DB flag ONCE (only if needed)
            User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }
    }

    /** Called when a login attempt succeeds. Resets counters. */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onAuthSuccess(String username) {
        // Clear attempt counter quickly (no DB hits)
        redis.delete(attemptsKey(username));

        // If somehow DB flag is still locked but Redis lock is gone, unlock DB.
        autoUnlockIfWindowExpired(username);
    }

    /**
     * Before authenticate(), call this to unlock the DB flag if the 30m window expired.
     * This avoids needing Redis keyspace notifications or a scheduler.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void autoUnlockIfWindowExpired(String username) {
        boolean lockExists = isLocked(username);
        if (!lockExists) {
            // If DB says locked but Redis lock window has elapsed, flip it back.
            userRepository.findByUsername(username).ifPresent(u -> {
                if (!u.isAccountNonLocked()) {
                    u.setAccountNonLocked(true);
                    userRepository.save(u);
                }
            });
        }
    }

    public long remainingAttempts(String username) {
        String attemptsKey = attemptsKey(username);
        String val = redis.opsForValue().get(attemptsKey);
        long attempts = (val == null) ? 0L : Long.parseLong(val);
        long remaining = Math.max(0, LOCK_AFTER - attempts);
        return remaining;
    }

    private String attemptsKey(String username) { return String.format(ATTEMPTS_KEY_FMT, username); }
    private String lockKey(String username)     { return String.format(LOCK_KEY_FMT, username); }
}
