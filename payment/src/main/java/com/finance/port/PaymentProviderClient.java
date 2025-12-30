package com.finance.port;

import com.finance.enums.PaymentProvider;
import com.finance.model.ProviderPaymentSession;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentProviderClient {
    ProviderPaymentSession initiatePayment(
            UUID paymentId,
            BigDecimal amount,
            String currency
    );

    PaymentProvider provider();
}
