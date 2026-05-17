CREATE TABLE energy_daily (
    date                DATE        PRIMARY KEY,
    total_wh            DOUBLE PRECISION NOT NULL DEFAULT 0,
    last_calculated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
