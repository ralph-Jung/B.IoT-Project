CREATE TABLE sensor_data (
    id          BIGSERIAL    PRIMARY KEY,
    sensor_id   BIGINT       NOT NULL,
    temperature DECIMAL(5,2),
    humidity    DECIMAL(5,2),
    motion      SMALLINT,
    status      VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),

    FOREIGN KEY (sensor_id) REFERENCES sensor(id)
);
