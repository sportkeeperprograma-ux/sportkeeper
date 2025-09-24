-- V1: esquema base
CREATE TABLE IF NOT EXISTS slot (
                                    id UUID PRIMARY KEY,
                                    start_at TIMESTAMP NOT NULL,
                                    end_at   TIMESTAMP NOT NULL,
                                    capacity INTEGER   NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_slot_start_at ON slot(start_at);
