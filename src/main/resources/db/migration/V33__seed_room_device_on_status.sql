-- 1층 회의실 조명, 에어컨 ON
UPDATE room_device rd
SET status = 'ON'
FROM room r, device d
WHERE rd.room_id = r.id AND rd.device_id = d.id
  AND r.name = '1층 회의실';

-- 1층 사무실 조명 ON
UPDATE room_device rd
SET status = 'ON'
FROM room r, device d
WHERE rd.room_id = r.id AND rd.device_id = d.id
  AND r.name = '1층 사무실' AND d.name = '조명';
