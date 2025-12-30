package com.finance.service;


import com.finance.model.WalletBalance;
import com.finance.port.WalletRepository;
import event.LedgerEntryCreated;
import event.enums.LedgerDirection;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletBalance applyLedgerEntry(LedgerEntryCreated event) {
        UUID userId = event.userId();

        WalletBalance current = walletRepository
                .findByUserId(userId)
                .orElseGet(() -> WalletBalance.builder()
                        .userId(userId)
                        .amount(BigDecimal.ZERO)
                        .currency(event.currency())
                        .build());

        BigDecimal delta = event.direction().name().equals(LedgerDirection.CREDIT.name())
                ? event.amount()
                : event.amount().negate();

        WalletBalance updated = current.apply(delta);
        return walletRepository.save(updated);
    }

    public WalletBalance getBalance(UUID userId) {
        return walletRepository.findByUserId(userId)
                .orElse(new WalletBalance(userId, BigDecimal.ZERO, "USD"));
    }
}
