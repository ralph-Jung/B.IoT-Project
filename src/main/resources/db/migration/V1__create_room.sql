CREATE TABLE room (
    id         BIGSERIAL    PRIMARY KEY,
    room_id    VARCHAR(50)  NOT NULL UNIQUE,
    name       VARCHAR(100) NOT NULL,
    floor      INTEGER,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

INSERT INTO room (room_id, name, floor) VALUES
    ('room-101', '1층 회의실', 1),
    ('room-102', '1층 사무실', 1),
    ('room-103', '2층 회의실', 2);
