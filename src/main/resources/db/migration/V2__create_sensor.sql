CREATE TABLE sensor (
    id         BIGSERIAL    PRIMARY KEY,
    sensor_id  VARCHAR(50)  NOT NULL UNIQUE,
    room_id    VARCHAR(50)  NOT NULL,
    type       VARCHAR(20)  NOT NULL,
    name       VARCHAR(100),
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),

    FOREIGN KEY (room_id) REFERENCES room(room_id)
);

INSERT INTO sensor (sensor_id, room_id, type, name) VALUES
    ('pir-101', 'room-101', 'PIR',   '1층 회의실 PIR 센서'),
    ('dht-101', 'room-101', 'DHT22', '1층 회의실 온습도 센서'),
    ('pir-102', 'room-102', 'PIR',   '1층 사무실 PIR 센서'),
    ('dht-102', 'room-102', 'DHT22', '1층 사무실 온습도 센서'),
    ('pir-103', 'room-103', 'PIR',   '2층 회의실 PIR 센서'),
    ('dht-103', 'room-103', 'DHT22', '2층 회의실 온습도 센서');
