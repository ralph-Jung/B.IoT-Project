-- room_device: status, mode
ALTER TABLE room_device
    ALTER COLUMN status TYPE VARCHAR(20) USING status::text,
    ALTER COLUMN mode   TYPE VARCHAR(20) USING mode::text;

-- alert_log: type
ALTER TABLE alert_log
    ALTER COLUMN type TYPE VARCHAR(30) USING type::text;

-- sensor_data: status
ALTER TABLE sensor_data
    ALTER COLUMN status TYPE VARCHAR(20) USING status::text;

