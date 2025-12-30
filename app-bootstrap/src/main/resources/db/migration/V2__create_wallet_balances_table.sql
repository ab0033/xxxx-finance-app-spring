create table if not exists wallet_balances
(
    user_id    uuid primary key,
    amount     numeric(19,4) not null,
    currency   varchar(16)   not null,
    updated_at timestamptz   not null
);

create index if not exists idx_wallet_balances_updated_at on wallet_balances (updated_at);