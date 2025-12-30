package com.finance.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Builder
@Getter
public final class WalletBalance {
    @NonNull
    private final UUID userId;
    @NonNull
    private final BigDecimal amount;
    @NonNull
    private final String currency;

    public WalletBalance apply(BigDecimal delta) {
        return WalletBalance.builder()
                .userId(userId)
                .amount(amount.add(delta))
                .currency(currency)
                .build();
    }
}
