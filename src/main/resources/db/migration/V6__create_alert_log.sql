CREATE TABLE alert_log (
    id         BIGSERIAL    PRIMARY KEY,
    room_id    VARCHAR(50)  NOT NULL,
    sensor_id  VARCHAR(50)  NOT NULL,
    type       VARCHAR(30)  NOT NULL,
    message    VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),

    FOREIGN KEY (room_id)   REFERENCES room(room_id),
    FOREIGN KEY (sensor_id) REFERENCES sensor(sensor_id)
);
