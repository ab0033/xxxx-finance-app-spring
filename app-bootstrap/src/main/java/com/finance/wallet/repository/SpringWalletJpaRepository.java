package com.finance.wallet.repository;

import com.finance.wallet.entity.WalletBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringWalletJpaRepository extends JpaRepository<WalletBalanceEntity, UUID> {
}
