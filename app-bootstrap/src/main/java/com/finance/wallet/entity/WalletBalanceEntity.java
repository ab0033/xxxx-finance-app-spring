package com.finance.wallet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "wallet_balances",
        indexes = {
                @Index(name = "idx_wallet_balances_updated_at", columnList = "updated_at")
        })
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletBalanceEntity {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 16)
    private String currency;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
