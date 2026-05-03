CREATE TABLE sensor (
    id             BIGSERIAL    PRIMARY KEY,
    identifier     VARCHAR(50)  NOT NULL UNIQUE,
    sensor_type_id BIGINT       NOT NULL,
    room_id        BIGINT       NOT NULL,
    name           VARCHAR(100),
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW(),

    FOREIGN KEY (sensor_type_id) REFERENCES sensor_type(id),
    FOREIGN KEY (room_id)        REFERENCES room(id)
);

INSERT INTO sensor (identifier, sensor_type_id, room_id, name)
SELECT 'pir-101', st.id, r.id, '1층 회의실 PIR 센서'
FROM sensor_type st, room r WHERE st.name = 'PIR' AND r.name = '1층 회의실'
UNION ALL
SELECT 'dht-101', st.id, r.id, '1층 회의실 온습도 센서'
FROM sensor_type st, room r WHERE st.name = 'DHT22' AND r.name = '1층 회의실'
UNION ALL
SELECT 'pir-102', st.id, r.id, '1층 사무실 PIR 센서'
FROM sensor_type st, room r WHERE st.name = 'PIR' AND r.name = '1층 사무실'
UNION ALL
SELECT 'dht-102', st.id, r.id, '1층 사무실 온습도 센서'
FROM sensor_type st, room r WHERE st.name = 'DHT22' AND r.name = '1층 사무실'
UNION ALL
SELECT 'pir-103', st.id, r.id, '2층 회의실 PIR 센서'
FROM sensor_type st, room r WHERE st.name = 'PIR' AND r.name = '2층 회의실'
UNION ALL
SELECT 'dht-103', st.id, r.id, '2층 회의실 온습도 센서'
FROM sensor_type st, room r WHERE st.name = 'DHT22' AND r.name = '2층 회의실';
