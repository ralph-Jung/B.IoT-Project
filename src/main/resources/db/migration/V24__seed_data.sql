-- sensor_data: 6 sensors x 5 rows = 30 rows
INSERT INTO sensor_data (sensor_id, temperature, humidity, motion, status, created_at)
SELECT
    s.id,
    CASE WHEN st.name = 'DHT22' THEN (20 + (gs.n * 3) % 15)::DECIMAL(5,2) ELSE NULL END,
    CASE WHEN st.name = 'DHT22' THEN (40 + (gs.n * 7) % 30)::DECIMAL(5,2) ELSE NULL END,
    CASE WHEN st.name = 'PIR'   THEN (gs.n % 2)::SMALLINT                  ELSE NULL END,
    CASE WHEN gs.n % 5 = 0 THEN 'ANOMALY'::sensor_status ELSE 'NORMAL'::sensor_status END,
    NOW() - (gs.n * 10 + s.id * 5) * INTERVAL '1 minute'
FROM sensor s
JOIN sensor_type st ON st.id = s.sensor_type_id
CROSS JOIN generate_series(1, 5) AS gs(n);

-- control_log: 30 rows
INSERT INTO control_log (room_id, device_id, action, type, created_at)
SELECT
    r.id,
    d.id,
    CASE WHEN gs.n % 2 = 0 THEN 'ON'::device_status  ELSE 'OFF'::device_status END,
    CASE WHEN gs.n % 3 = 0 THEN 'MANUAL'::control_mode ELSE 'AUTO'::control_mode END,
    NOW() - gs.n * INTERVAL '30 minutes'
FROM generate_series(1, 30) AS gs(n)
CROSS JOIN LATERAL (SELECT id FROM room   ORDER BY id LIMIT 1 OFFSET (gs.n - 1) % 3) AS r
CROSS JOIN LATERAL (SELECT id FROM device ORDER BY id LIMIT 1 OFFSET (gs.n - 1) % 2) AS d;

-- alert_log: 30 rows
INSERT INTO alert_log (sensor_id, type, message, created_at)
SELECT
    s.id,
    CASE gs.n % 3
        WHEN 0 THEN 'HIGH_TEMP'::alert_type
        WHEN 1 THEN 'HIGH_HUMIDITY'::alert_type
        ELSE        'NIGHT_MOTION'::alert_type
    END,
    CASE gs.n % 3
        WHEN 0 THEN '온도가 임계값을 초과했습니다.'
        WHEN 1 THEN '습도가 임계값을 초과했습니다.'
        ELSE        '야간 모션이 감지되었습니다.'
    END,
    NOW() - gs.n * INTERVAL '1 hour'
FROM generate_series(1, 30) AS gs(n)
CROSS JOIN LATERAL (SELECT id FROM sensor ORDER BY id LIMIT 1 OFFSET (gs.n - 1) % 6) AS s;
