CREATE TABLE room_device (
    id         BIGSERIAL   PRIMARY KEY,
    room_id    VARCHAR(50) NOT NULL,
    device     VARCHAR(20) NOT NULL,
    status     VARCHAR(10) NOT NULL DEFAULT 'OFF',
    mode       VARCHAR(10) NOT NULL DEFAULT 'AUTO',
    updated_at TIMESTAMP   NOT NULL DEFAULT NOW(),

    FOREIGN KEY (room_id) REFERENCES room(room_id),
    UNIQUE (room_id, device)
);

INSERT INTO room_device (room_id, device, status, mode) VALUES
    ('room-101', 'LIGHT', 'OFF', 'AUTO'),
    ('room-101', 'AC',    'OFF', 'AUTO'),
    ('room-102', 'LIGHT', 'OFF', 'AUTO'),
    ('room-102', 'AC',    'OFF', 'AUTO'),
    ('room-103', 'LIGHT', 'OFF', 'AUTO'),
    ('room-103', 'AC',    'OFF', 'AUTO');
