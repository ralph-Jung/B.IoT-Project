CREATE INDEX idx_sensor_data_sensor_created ON sensor_data (sensor_id, created_at);

CREATE INDEX idx_control_log_room_created ON control_log (room_id, created_at);

CREATE INDEX idx_alert_log_sensor_created ON alert_log (sensor_id, created_at);
