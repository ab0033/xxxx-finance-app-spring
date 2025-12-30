package com.finance.wallet.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletBalanceResponse(
        UUID userId,
        BigDecimal amount,
        String currency
) {
}
