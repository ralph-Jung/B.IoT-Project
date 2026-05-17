ALTER TABLE control_log
    ALTER COLUMN action TYPE varchar(10) USING action::text,
    ALTER COLUMN type   TYPE varchar(10) USING type::text;
