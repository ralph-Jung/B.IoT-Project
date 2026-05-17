CREATE TYPE sensor_status AS ENUM ('NORMAL', 'ANOMALY');

ALTER TABLE sensor_data
    ALTER COLUMN status TYPE sensor_status USING status::sensor_status;
