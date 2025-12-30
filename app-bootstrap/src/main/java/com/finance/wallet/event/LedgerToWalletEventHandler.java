package com.finance.wallet.event;

import event.LedgerEntryCreated;
import com.finance.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LedgerToWalletEventHandler {
    private final WalletService walletService;

    @EventListener
    public void handle(LedgerEntryCreated event) {
        walletService.applyLedgerEntry(event);
    }
}
