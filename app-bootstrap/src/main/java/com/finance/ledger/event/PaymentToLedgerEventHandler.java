package com.finance.ledger.event;

import event.PaymentSucceeded;
import com.finance.ledger.service.LedgerFacade;
import com.finance.model.Money;
import com.finance.model.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentToLedgerEventHandler {
    private final LedgerFacade ledgerFacade;

    @EventListener
    public void handle(PaymentSucceeded event) {

        ledgerFacade.applyTopUp(
                new UserId(event.userId()),
                Money.of(event.amount(), event.currency()),
                event.referenceId() // paymentId → идемпотентность ledger
        );
    }
}
