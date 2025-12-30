package com.finance.payment.mapper;

import com.finance.model.Payment;
import com.finance.payment.entity.PaymentEntity;

public interface PaymentMapper {
    PaymentEntity toEntity(Payment p);

    Payment toDomain(PaymentEntity e);
}
