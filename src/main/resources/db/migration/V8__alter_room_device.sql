DROP TABLE room_device;

CREATE TABLE room_device (
    id         BIGSERIAL   PRIMARY KEY,
    room_id    VARCHAR(50) NOT NULL,
    device_id  BIGINT      NOT NULL,
    status     VARCHAR(10) NOT NULL DEFAULT 'OFF',
    mode       VARCHAR(10) NOT NULL DEFAULT 'AUTO',
    updated_at TIMESTAMP   NOT NULL DEFAULT NOW(),

    FOREIGN KEY (room_id)   REFERENCES room(room_id),
    FOREIGN KEY (device_id) REFERENCES device(id),
    UNIQUE (room_id, device_id)
);

INSERT INTO room_device (room_id, device_id, status, mode)
SELECT r.room_id, d.id, 'OFF', 'AUTO'
FROM room r
CROSS JOIN device d;
