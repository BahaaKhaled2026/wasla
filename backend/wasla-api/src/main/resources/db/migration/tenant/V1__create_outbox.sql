CREATE TABLE outbox_event (
    event_id         UUID PRIMARY KEY,
    aggregate_type    TEXT NOT NULL,
    aggregate_id      TEXT NOT NULL,
    event_type        TEXT NOT NULL,
    event_version     INTEGER NOT NULL,
    payload           JSONB NOT NULL,
    correlation_id    UUID NOT NULL,
    causation_id      UUID,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    published_at      TIMESTAMPTZ,
    publish_attempts  INTEGER NOT NULL DEFAULT 0,
    next_attempt_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    last_error        TEXT
);

CREATE INDEX idx_outbox_event_unpublished
    ON outbox_event (next_attempt_at)
    WHERE published_at IS NULL;

CREATE INDEX idx_outbox_event_aggregate
    ON outbox_event (aggregate_type, aggregate_id);