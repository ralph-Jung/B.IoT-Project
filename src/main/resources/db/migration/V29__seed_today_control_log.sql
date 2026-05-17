-- 오늘 날짜 기준 control_log 시드 데이터
-- 조명: 2시간 30분 전 ON → 1시간 30분 전 OFF (1시간 사용 → 50Wh)
INSERT INTO control_log (room_id, device_id, action, type, created_at)
SELECT r.id, d.id, 'ON', 'AUTO', NOW() - INTERVAL '2.5 hours'
FROM room r, device d
WHERE r.name = '1층 회의실' AND d.name = '조명';

INSERT INTO control_log (room_id, device_id, action, type, created_at)
SELECT r.id, d.id, 'OFF', 'AUTO', NOW() - INTERVAL '1.5 hours'
FROM room r, device d
WHERE r.name = '1층 회의실' AND d.name = '조명';

-- 에어컨: 2시간 전 ON → 30분 전 OFF (1.5시간 사용 → 2250Wh)
INSERT INTO control_log (room_id, device_id, action, type, created_at)
SELECT r.id, d.id, 'ON', 'MANUAL', NOW() - INTERVAL '2 hours'
FROM room r, device d
WHERE r.name = '1층 회의실' AND d.name = '에어컨';

INSERT INTO control_log (room_id, device_id, action, type, created_at)
SELECT r.id, d.id, 'OFF', 'MANUAL', NOW() - INTERVAL '30 minutes'
FROM room r, device d
WHERE r.name = '1층 회의실' AND d.name = '에어컨';

-- 조명: 20분 전 ON (현재까지 켜져 있는 상태)
INSERT INTO control_log (room_id, device_id, action, type, created_at)
SELECT r.id, d.id, 'ON', 'AUTO', NOW() - INTERVAL '20 minutes'
FROM room r, device d
WHERE r.name = '1층 사무실' AND d.name = '조명';
