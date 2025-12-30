package com.finance.model;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
    private static final int SCALE = 2;
    private final BigDecimal amount;
    private final String currency;

    private Money(BigDecimal amount, String currency) {
        this.amount = amount.setScale(SCALE, RoundingMode.HALF_EVEN);
        this.currency = Objects.requireNonNull(currency, "Currency must not be null");
    }

    public static Money of(BigDecimal amount, String currency) {
        Objects.requireNonNull(amount, "Amount must not be null");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        return new Money(amount, currency);
    }

    public Money negate() {
        return new Money(amount.negate(), currency);
    }

    public BigDecimal amount() {
        return amount;
    }

    public String currency() {
        return currency;
    }


    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
