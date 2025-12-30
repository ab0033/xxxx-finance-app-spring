package com.finance.payment.dto;

import com.finance.enums.PaymentProvider;
import com.finance.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class InitiatePaymentResponse {
    PaymentStatus status;
    String providerPaymentId;
    String providerSecret;
    String currency;
    String amount;
    UUID paymentId;
    PaymentProvider provider;
}
