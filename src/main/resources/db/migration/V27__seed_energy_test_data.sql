-- energy_daily: 오늘 행, lastCalculatedAt = 3시간 전
INSERT INTO energy_daily (date, total_wh, last_calculated_at)
VALUES (CURRENT_DATE, 500.0, NOW() - INTERVAL '3 hours');

-- control_log 테스트 데이터 (lastCalculatedAt 이후 구간에 ON/OFF 쌍 생성)
-- 조명: 2시간 30분 전 ON → 1시간 30분 전 OFF (1시간 사용 → 50Wh)
INSERT INTO control_log (room_id, device_id, action, type, created_at)
SELECT r.id, d.id, 'ON'::device_status, 'AUTO'::control_mode, NOW() - INTERVAL '2.5 hours'
FROM room r, device d
WHERE r.name = '1층 회의실' AND d.name = '조명';

INSERT INTO control_log (room_id, device_id, action, type, created_at)
SELECT r.id, d.id, 'OFF'::device_status, 'AUTO'::control_mode, NOW() - INTERVAL '1.5 hours'
FROM room r, device d
WHERE r.name = '1층 회의실' AND d.name = '조명';

-- 에어컨: 2시간 전 ON → 30분 전 OFF (1.5시간 사용 → 2250Wh)
INSERT INTO control_log (room_id, device_id, action, type, created_at)
SELECT r.id, d.id, 'ON'::device_status, 'MANUAL'::control_mode, NOW() - INTERVAL '2 hours'
FROM room r, device d
WHERE r.name = '1층 회의실' AND d.name = '에어컨';

INSERT INTO control_log (room_id, device_id, action, type, created_at)
SELECT r.id, d.id, 'OFF'::device_status, 'MANUAL'::control_mode, NOW() - INTERVAL '30 minutes'
FROM room r, device d
WHERE r.name = '1층 회의실' AND d.name = '에어컨';

-- 조명: 20분 전 ON (OFF 없음 → 현재까지 계산)
INSERT INTO control_log (room_id, device_id, action, type, created_at)
SELECT r.id, d.id, 'ON'::device_status, 'AUTO'::control_mode, NOW() - INTERVAL '20 minutes'
FROM room r, device d
WHERE r.name = '1층 사무실' AND d.name = '조명';
