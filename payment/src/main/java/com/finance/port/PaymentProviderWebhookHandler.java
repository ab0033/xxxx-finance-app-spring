package com.finance.port;

import com.finance.enums.PaymentProvider;

public interface PaymentProviderWebhookHandler {
    void handle(String payload);

    PaymentProvider provider();
}
