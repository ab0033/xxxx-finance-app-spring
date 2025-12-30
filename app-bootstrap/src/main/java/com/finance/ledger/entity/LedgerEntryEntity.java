package com.finance.ledger.entity;

import event.enums.LedgerDirection;
import event.enums.LedgerReason;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "ledger_entries",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_ledger_reference",
                columnNames = {"reference_id", "reason"}
        ),
        indexes = {
                @Index(name = "idx_ledger_entries_user_id", columnList = "user_id"),
                @Index(name = "idx_ledger_entries_created_at", columnList = "created_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LedgerEntryEntity {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private LedgerDirection direction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private LedgerReason reason;

    @Column(name = "reference_id", nullable = false)
    private UUID referenceId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
