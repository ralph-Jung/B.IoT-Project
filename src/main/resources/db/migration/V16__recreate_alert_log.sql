CREATE TABLE alert_log (
    id         BIGSERIAL    PRIMARY KEY,
    sensor_id  BIGINT       NOT NULL,
    type       VARCHAR(30)  NOT NULL,
    message    VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),

    FOREIGN KEY (sensor_id) REFERENCES sensor(id)
);
