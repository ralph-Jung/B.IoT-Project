CREATE TABLE room_device (
    room_id    BIGINT      NOT NULL,
    device_id  BIGINT      NOT NULL,
    status     VARCHAR(10) NOT NULL DEFAULT 'OFF',
    mode       VARCHAR(10) NOT NULL DEFAULT 'AUTO',
    updated_at TIMESTAMP   NOT NULL DEFAULT NOW(),

    PRIMARY KEY (room_id, device_id),
    FOREIGN KEY (room_id)  REFERENCES room(id),
    FOREIGN KEY (device_id) REFERENCES device(id)
);

INSERT INTO room_device (room_id, device_id, status, mode)
SELECT r.id, d.id, 'OFF', 'AUTO'
FROM room r
CROSS JOIN device d;
