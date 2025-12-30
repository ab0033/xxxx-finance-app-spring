package com.finance.model;

import java.util.UUID;

public record UserId(UUID value) {
    public UserId {
        if (value == null) throw new IllegalArgumentException("Value is null or empty");
    }
}
