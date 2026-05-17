CREATE SEQUENCE users_id_seq;

CREATE TABLE users (
    id         BIGINT       PRIMARY KEY DEFAULT nextval('users_id_seq'::regclass),
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);
