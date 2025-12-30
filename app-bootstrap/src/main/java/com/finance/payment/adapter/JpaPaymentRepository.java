package com.finance.payment.adapter;

import com.finance.model.Payment;
import com.finance.payment.entity.PaymentEntity;
import com.finance.payment.mapper.PaymentMapper;
import com.finance.payment.repository.SpringPaymentJpaRepository;
import com.finance.port.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaPaymentRepository implements PaymentRepository {
    private final SpringPaymentJpaRepository jpa;
    private final PaymentMapper mapper;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity saved =
                jpa.save(mapper.toEntity(payment));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Payment> findById(UUID paymentId) {
        return jpa.findById(paymentId).map(mapper::toDomain);
    }

    @Override
    public Optional<Payment> findByIdempotencyKey(UUID key) {
        return jpa.findByIdempotencyKey(key).map(mapper::toDomain);
    }
}
