-- 어제 누적 전력량 데이터 삽입 (어제 대비 비교 테스트용)
-- 조명 3대(500W) × 11h + 에어컨(5000W) × 3h = 16,500 + 15,000 = 31,500 Wh
INSERT INTO energy_daily (date, total_wh, last_calculated_at)
VALUES (CURRENT_DATE - 1, 31500.0, NOW() - INTERVAL '1 day')
ON CONFLICT (date) DO NOTHING;
