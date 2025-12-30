package com.finance.wallet.cache;

import com.finance.model.WalletBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class RedisWalletCache {
    private static final Duration TTL = Duration.ofSeconds(30);
    private final RedisTemplate<String, WalletBalance> redis;

    public Optional<WalletBalance> get(UUID userId) {
        return Optional.ofNullable(redis.opsForValue().get(userId.toString()));
    }

    public void put(WalletBalance walletBalance) {
        redis.opsForValue().set(key(walletBalance.getUserId()), walletBalance, TTL);
    }

    private String key(UUID userId) {
        return "wallet:balance:" + userId;
    }
}
