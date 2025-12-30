package com.finance.wallet.adapter;

import com.finance.model.WalletBalance;
import com.finance.port.WalletRepository;
import com.finance.wallet.cache.RedisWalletCache;
import com.finance.wallet.entity.WalletBalanceEntity;
import com.finance.wallet.mapper.WalletBalanceMapper;
import com.finance.wallet.repository.SpringWalletJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CachedWalletRepository implements WalletRepository {
    private final RedisWalletCache redisWalletCache;
    private final WalletBalanceMapper mapper;
    private final SpringWalletJpaRepository jpaRepository;

    @Override
    public Optional<WalletBalance> findByUserId(UUID userId) {
        Optional<WalletBalance> cached = redisWalletCache.get(userId);
        if (cached.isPresent()) {
            return cached;
        }
        Optional<WalletBalance> fromDb = jpaRepository.findById(userId).map(mapper::toDomain);
        fromDb.ifPresent(redisWalletCache::put);

        return fromDb;
    }

    @Override
    public WalletBalance save(WalletBalance balance) {
        WalletBalanceEntity entity = jpaRepository.save(mapper.toEntity(balance));

        WalletBalance domain = mapper.toDomain(entity);

        redisWalletCache.put(domain);

        return domain;
    }
}
