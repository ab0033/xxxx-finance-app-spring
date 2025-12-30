package com.finance.port;

import com.finance.model.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(UUID paymentId);

    Optional<Payment> findByIdempotencyKey(UUID key);

}
