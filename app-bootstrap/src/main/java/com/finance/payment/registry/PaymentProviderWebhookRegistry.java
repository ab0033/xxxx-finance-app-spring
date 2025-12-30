package com.finance.payment.registry;

import com.finance.enums.PaymentProvider;
import com.finance.port.PaymentProviderWebhookHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PaymentProviderWebhookRegistry {
    private final Map<PaymentProvider, PaymentProviderWebhookHandler> providerHandlers;

    public PaymentProviderWebhookRegistry(List<PaymentProviderWebhookHandler> handlers) {
        providerHandlers = handlers.stream()
                .collect(Collectors.toMap(
                        PaymentProviderWebhookHandler::provider,
                        handler -> handler
                ));
    }

    public PaymentProviderWebhookHandler get(PaymentProvider provider) {
        PaymentProviderWebhookHandler handler = providerHandlers.get(provider);
        if (handler == null) {
            throw new IllegalStateException("Unsupported payment provider: " + provider);
        }

        return handler;
    }
}
