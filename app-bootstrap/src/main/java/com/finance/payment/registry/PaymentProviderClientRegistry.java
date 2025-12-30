package com.finance.payment.registry;

import com.finance.enums.PaymentProvider;
import com.finance.port.PaymentProviderClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PaymentProviderClientRegistry {
    private final Map<PaymentProvider, PaymentProviderClient> providers;

    public PaymentProviderClientRegistry(List<PaymentProviderClient> clients) {
        this.providers = clients.stream()
                .collect(Collectors.toMap(
                        PaymentProviderClient::provider,
                        providerClient -> providerClient));
    }

    public PaymentProviderClient get(PaymentProvider provider) {
        PaymentProviderClient client = providers.get(provider);
        if (client == null) {
            throw new IllegalArgumentException("Unsupported payment provider: " + provider);
        }

        return client;
    }
}
