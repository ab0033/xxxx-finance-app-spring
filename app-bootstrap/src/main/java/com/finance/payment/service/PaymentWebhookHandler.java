package com.finance.payment.service;

import com.finance.enums.PaymentProvider;
import com.finance.payment.registry.PaymentProviderWebhookRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentWebhookHandler {
    private final PaymentProviderWebhookRegistry registry;

    public void handle(String payload, PaymentProvider provider) {
        registry.get(provider).handle(payload);
    }
}
