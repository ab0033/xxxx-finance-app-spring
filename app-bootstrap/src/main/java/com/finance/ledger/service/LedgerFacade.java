package com.finance.ledger.service;

import com.finance.model.LedgerEntry;
import com.finance.model.Money;
import com.finance.model.UserId;
import com.finance.service.LedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerFacade {
    private final LedgerService ledgerService;

    @Transactional
    public LedgerEntry applyTopUp(UserId userId, Money money, UUID referenceId) {
        return ledgerService.applyTopUp(userId, money, referenceId);
    }

    @Transactional
    public LedgerEntry applyRefund(UserId userId, Money money, UUID referenceId) {
        return ledgerService.applyRefund(userId, money, referenceId);
    }
}
