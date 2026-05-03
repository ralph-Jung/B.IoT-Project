CREATE TABLE control_log (
    id         BIGSERIAL   PRIMARY KEY,
    room_id    BIGINT      NOT NULL,
    device_id  BIGINT      NOT NULL,
    action     VARCHAR(10) NOT NULL,
    type       VARCHAR(10) NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),

    FOREIGN KEY (room_id)  REFERENCES room(id),
    FOREIGN KEY (device_id) REFERENCES device(id)
);
