CREATE TABLE sensor_type (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

INSERT INTO sensor_type (name, description) VALUES
    ('PIR',   'PIR 모션 센서'),
    ('DHT22', '온습도 센서');
