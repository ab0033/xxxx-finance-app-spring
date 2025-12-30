package com.finance.payment.entity;

import com.finance.enums.PaymentProvider;
import com.finance.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "payments",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_provider_payment",
                columnNames = {"provider", "provider_payment_id"}
        ),
        indexes = {
                @Index(name = "idx_payments_idempotency_key", columnList = "idempotency_key")
        })
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PaymentEntity {
    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PaymentProvider provider;

    @Column(name = "provider_payment_id", length = 100)
    private String providerPaymentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "provider_payment_status", length = 50)
    private String providerPaymentStatus;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "idempotency_key", unique = true, nullable = false)
    private UUID idempotencyKey;
}
