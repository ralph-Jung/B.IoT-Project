ALTER TABLE energy_daily DROP CONSTRAINT energy_daily_pkey;

ALTER TABLE energy_daily ADD COLUMN id BIGSERIAL PRIMARY KEY;

ALTER TABLE energy_daily ADD CONSTRAINT uq_energy_daily_date UNIQUE (date);
