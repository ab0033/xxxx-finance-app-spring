package com.finance.service;

import com.finance.enums.PaymentProvider;
import com.finance.enums.PaymentStatus;
import com.finance.model.Payment;
import com.finance.port.PaymentEventPublisher;
import com.finance.port.PaymentRepository;
import event.PaymentSucceeded;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public final class PaymentService {
    private final PaymentRepository repository;
    private final PaymentEventPublisher publisher;
    private final Clock clock;

    public Payment initiatePayment(
            UUID userId,
            PaymentProvider provider,
            String currency,
            BigDecimal amount,
            UUID idempotencyKey
    ) {
        Optional<Payment> existing = repository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) return existing.get();

        Payment payment = new Payment(
                null,
                userId,
                amount,
                currency,
                provider,
                null,
                PaymentStatus.INITIATED,
                null,
                Instant.now(clock),
                idempotencyKey
        );
        return repository.save(payment);
    }

    public Payment markCreated(Payment payment,
                               String providerPaymentId,
                               String providerPaymentStatus) {
        Objects.requireNonNull(payment.getId(), "PaymentId must not be null");
        Objects.requireNonNull(providerPaymentId, "ProviderPaymentId must not be null");
        Objects.requireNonNull(providerPaymentStatus, "ProviderPaymentStatus must not be null");

        Payment created = payment.markCreated(providerPaymentId, providerPaymentStatus);
        repository.save(created);

        return created;
    }

    /** PaymentId comes from stripe metadata we set when creating initiate payment request */
    public Payment markSucceeded(UUID paymentId, String providerPaymentStatus) {
        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new IllegalStateException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.FAILED ||
                payment.getStatus() == PaymentStatus.SUCCEEDED) {
            return payment;
        }

        Payment succeeded = payment.markSucceeded(providerPaymentStatus);
        repository.save(succeeded);

        publisher.publish(new PaymentSucceeded(
                UUID.randomUUID(),
                succeeded.getId(),
                succeeded.getUserId(),
                succeeded.getAmount(),
                succeeded.getCurrency(),
                succeeded.getId(), // referenceId for ledger
                Instant.now(clock)
        ));

        return succeeded;
    }

    /** PaymentId comes from stripe metadata we set when creating initiate payment request */
    public Payment markFailed(UUID paymentId, String providerPaymentStatus) {
        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new IllegalStateException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.FAILED ||
                payment.getStatus() == PaymentStatus.SUCCEEDED) {
            return payment;
        }

        Payment failed = payment.markFailed(providerPaymentStatus);
        repository.save(failed);

        return failed;
    }
}
