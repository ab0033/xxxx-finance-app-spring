package com.finance.payment.dto;

import com.finance.enums.PaymentProvider;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InitiatePaymentRequest {
    UUID userId;
    PaymentProvider provider;
    String currency;
    BigDecimal amount;
    UUID idempotencyKey;
    String paymentMethod;
}
