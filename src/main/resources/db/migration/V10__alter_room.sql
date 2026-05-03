-- Drop all tables that hold FK references to room.room_id
DROP TABLE alert_log;
DROP TABLE control_log;
DROP TABLE sensor_data;
DROP TABLE room_device;
DROP TABLE sensor;

-- Remove the legacy business-key column; id (BIGSERIAL) becomes the sole PK
ALTER TABLE room DROP COLUMN room_id;
