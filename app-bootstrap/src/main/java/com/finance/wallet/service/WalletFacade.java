package com.finance.wallet.service;

import com.finance.model.WalletBalance;
import com.finance.service.WalletService;
import com.finance.wallet.dto.WalletBalanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletFacade {
    private final WalletService walletService;

    @Transactional(readOnly = true)
    public WalletBalanceResponse getBalance(UUID userId) {
        WalletBalance balance = walletService.getBalance(userId);
        return new WalletBalanceResponse(
                balance.getUserId(),
                balance.getAmount(),
                balance.getCurrency()
        );
    }
}
