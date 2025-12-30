package com.finance.wallet.mapper;

import com.finance.model.WalletBalance;
import com.finance.wallet.entity.WalletBalanceEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class WalletBalanceMapperImpl implements WalletBalanceMapper {

    public WalletBalance toDomain(WalletBalanceEntity e) {
        return WalletBalance.builder()
                .userId(e.getUserId())
                .amount(e.getAmount())
                .currency(e.getCurrency())
                .build();
    }

    public WalletBalanceEntity toEntity(WalletBalance b) {
        return WalletBalanceEntity.builder()
                .userId(b.getUserId())
                .amount(b.getAmount())
                .currency(b.getCurrency())
                .updatedAt(Instant.now())
                .build();
    }

}
