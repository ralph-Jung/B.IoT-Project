CREATE TYPE device_status AS ENUM ('ON', 'OFF');
CREATE TYPE control_mode  AS ENUM ('AUTO', 'MANUAL');
CREATE TYPE alert_type    AS ENUM ('HIGH_TEMP', 'HIGH_HUMIDITY', 'NIGHT_MOTION');

ALTER TABLE room_device
    ALTER COLUMN status TYPE device_status USING status::device_status,
    ALTER COLUMN mode   TYPE control_mode  USING mode::control_mode;

ALTER TABLE control_log
    ALTER COLUMN action TYPE device_status USING action::device_status,
    ALTER COLUMN type   TYPE control_mode  USING type::control_mode;

ALTER TABLE alert_log
    ALTER COLUMN type TYPE alert_type USING type::alert_type;
