ALTER TABLE device DROP COLUMN device_id;
ALTER TABLE device DROP COLUMN type;

ALTER TABLE device ALTER COLUMN name SET NOT NULL;
ALTER TABLE device ADD CONSTRAINT device_name_unique UNIQUE (name);

ALTER TABLE device ADD COLUMN power_watt INTEGER;
UPDATE device SET power_watt = 50   WHERE name = '조명';
UPDATE device SET power_watt = 1500 WHERE name = '에어컨';
ALTER TABLE device ALTER COLUMN power_watt SET NOT NULL;
