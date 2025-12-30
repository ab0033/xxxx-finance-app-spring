create table if not exists payments
(
    id                      uuid primary key,
    user_id                 uuid           not null,
    amount                  numeric(19, 4) not null,
    currency                varchar(10)    not null,
    provider                varchar(50)    not null,
    provider_payment_id     varchar(100),
    status                  varchar(20)    not null,
    provider_payment_status varchar(50),
    created_at              timestamptz    not null,
    idempotency_key         uuid unique    not null,

    constraint uq_provider_payment unique (provider, provider_payment_id)
);

create index if not exists idx_payments_idempotency_key on payments (idempotency_key);