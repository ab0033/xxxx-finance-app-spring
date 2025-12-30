package com.finance.model;

import com.finance.enums.PaymentProvider;
import com.finance.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Builder(toBuilder = true)
@Getter
public final class Payment {
    private final UUID id;
    private final UUID userId;
    private final BigDecimal amount;
    private final String currency;
    private final PaymentProvider provider;
    private final String providerPaymentId;
    private final PaymentStatus status;
    private final String providerPaymentStatus;
    private final Instant createdAt;
    private final UUID idempotencyKey;

    public Payment markCreated(String newProviderPaymentId,
                               String newProviderPaymentStatus) {
        if (status == PaymentStatus.SUCCEEDED
                || status == PaymentStatus.FAILED
                || status == PaymentStatus.CREATED) {
            return this;
        }

        return this.toBuilder()
                .providerPaymentId(newProviderPaymentId)
                .providerPaymentStatus(newProviderPaymentStatus)
                .status(PaymentStatus.CREATED)
                .build();
    }

    public Payment markSucceeded(String providerPaymentStatus) {
        return this.toBuilder()
                .status(PaymentStatus.SUCCEEDED)
                .providerPaymentStatus(providerPaymentStatus)
                .build();
    }

    public Payment markFailed(String providerPaymentStatus) {
        return this.toBuilder()
                .status(PaymentStatus.FAILED)
                .providerPaymentStatus(providerPaymentStatus)
                .build();
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", userId=" + userId +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", provider=" + provider +
                ", providerPaymentId='" + providerPaymentId + '\'' +
                ", status=" + status +
                ", providerPaymentStatus='" + providerPaymentStatus + '\'' +
                ", createdAt=" + createdAt +
                ", idempotencyKey=" + idempotencyKey +
                '}';
    }
}
