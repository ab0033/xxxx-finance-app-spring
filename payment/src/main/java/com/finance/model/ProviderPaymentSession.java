package com.finance.model;

public record ProviderPaymentSession(
        String providerSecret,
        String providerPaymentId,
        String status
) {
}
