CREATE TABLE control_log (
    id         BIGSERIAL   PRIMARY KEY,
    room_id    VARCHAR(50) NOT NULL,
    device     VARCHAR(20) NOT NULL,
    action     VARCHAR(10) NOT NULL,
    type       VARCHAR(10) NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),

    FOREIGN KEY (room_id) REFERENCES room(room_id)
);
