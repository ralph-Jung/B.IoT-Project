INSERT INTO users (email, password)
VALUES ('admin@gachon.ac.kr', 'admin1234')
ON CONFLICT DO NOTHING;
