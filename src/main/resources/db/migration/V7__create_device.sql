CREATE TABLE device (
    id         BIGSERIAL    PRIMARY KEY,
    device_id  VARCHAR(50)  NOT NULL UNIQUE,
    type       VARCHAR(20)  NOT NULL,
    name       VARCHAR(100),
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

INSERT INTO device (device_id, type, name) VALUES
    ('device-light', 'LIGHT', '조명'),
    ('device-ac',    'AC',    '에어컨');
