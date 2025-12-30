package com.finance.port;

import com.finance.model.WalletBalance;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
    Optional<WalletBalance> findByUserId(UUID userId);

    WalletBalance save(WalletBalance balance);
}