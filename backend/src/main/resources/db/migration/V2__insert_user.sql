
INSERT INTO users (email, password, name, phone_number, role, is_active)
VALUES 
(
    'admin123@gmail.com', 
    '$2a$10$JTCvQp9UG1LwMrUB554.qOmxvFO9ke/uOjCm/6apOxybfjoz0Fd2.', 
    'Quản trị viên', 
    '0901234567', 
    'ADMIN',
    true
),
(
    'user123@gmail.com', 
    '$2a$10$JTCvQp9UG1LwMrUB554.qOmxvFO9ke/uOjCm/6apOxybfjoz0Fd2.', 
    'người dùng', 
    '0987654321', 
    'USER', 
    true
);