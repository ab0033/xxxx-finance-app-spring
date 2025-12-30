package com.finance.ledger.event;

import event.PaymentDebited;
import com.finance.ledger.service.LedgerFacade;
import com.finance.model.Money;
import com.finance.model.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentDebitEventHandler {
    private final LedgerFacade ledgerFacade;

    @EventListener
    public void handle(PaymentDebited event) {
        ledgerFacade.applyRefund(
                new UserId(event.userId()),
                Money.of(event.amount(), event.currency()),
                event.referenceId() // paymentId → идемпотентность ledger
        );
    }

}
