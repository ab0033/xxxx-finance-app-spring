create table if not exists ledger_entries
(
    id           UUID primary key,
    user_id      UUID                     not null,
    amount       numeric(19, 4)           not null,
    currency     varchar(3)               not null,
    direction    varchar(32)              not null,
    reason       varchar(64)              not null,
    reference_id UUID                     not null,
    created_at   timestamp with time zone not null,
    constraint uq_ledger_reference unique (reference_id, reason)
);

create index if not exists idx_ledger_entries_user_id on ledger_entries (user_id);
create index if not exists idx_ledger_entries_created_at on ledger_entries (created_at);