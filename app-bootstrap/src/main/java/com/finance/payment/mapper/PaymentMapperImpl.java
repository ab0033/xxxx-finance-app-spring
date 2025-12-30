package com.finance.payment.mapper;

import com.finance.model.Payment;
import com.finance.payment.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapperImpl implements PaymentMapper {

    public PaymentEntity toEntity(Payment p) {
        return PaymentEntity.builder()
                .id(p.getId())
                .userId(p.getUserId())
                .amount(p.getAmount())
                .currency(p.getCurrency())
                .provider(p.getProvider())
                .providerPaymentId(p.getProviderPaymentId())
                .status(p.getStatus())
                .providerPaymentStatus(p.getProviderPaymentStatus())
                .createdAt(p.getCreatedAt())
                .idempotencyKey(p.getIdempotencyKey())
                .build();
    }

    public Payment toDomain(PaymentEntity e) {
        return Payment.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .amount(e.getAmount())
                .currency(e.getCurrency())
                .provider(e.getProvider())
                .providerPaymentId(e.getProviderPaymentId())
                .status(e.getStatus())
                .providerPaymentStatus(e.getProviderPaymentStatus())
                .createdAt(e.getCreatedAt())
                .idempotencyKey(e.getIdempotencyKey())
                .build();
    }
}
