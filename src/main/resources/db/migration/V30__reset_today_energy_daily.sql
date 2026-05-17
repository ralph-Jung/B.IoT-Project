-- 오늘 energy_daily 레코드 삭제 (서버 재시작 시 00:00 UTC 기준으로 재생성되도록)
DELETE FROM energy_daily WHERE date = CURRENT_DATE;
