-- ============================================================
--  CampusConnect — Seed Data  (EXPANDED)
--  Run AFTER the app has started once (Hibernate creates tables).
--
--  Usage:
--    psql -U postgres -d ccdb -f seed.sql
--
--  Password for ALL users:  Password@123
--  BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy
--
--  Tenants:
--    SEC  — Sunrise Engineering College      (11111111-...)
--    PIT  — Pioneer Institute of Technology  (22222222-...)
--    GCA  — Global College of Arts & Science (33333333-...)
-- ============================================================


-- ────────────────────────────────────────────────────────────
-- 1.  CLEAN  (safe re-run)
-- ────────────────────────────────────────────────────────────
TRUNCATE TABLE
    notifications,
    attendance,
    exam_enrolled_students,
    results,
    result_details,
    exams,
    materials,
    events,
    timetable_entries,
    courses,
    departments,
    users,
    otp_tokens,
    tenants
RESTART IDENTITY CASCADE;

-- ────────────────────────────────────────────────────────────
-- 1.  TENANTS  (3 colleges)
-- ────────────────────────────────────────────────────────────
INSERT INTO tenants (id, name, code, contact_email, contact_phone, address, subscription_plan, timezone, enabled, created_at, updated_at) VALUES
(
    '11111111-1111-1111-1111-111111111111',
    'Sunrise Engineering College', 'SEC',
    'admin@sunrise.edu', '+91-9000000001',
    '42, College Road, Pune, Maharashtra - 411001',
    'PRO', 'Asia/Kolkata', true, NOW(), NOW()
),
(
    '22222222-2222-2222-2222-222222222222',
    'Pioneer Institute of Technology', 'PIT',
    'admin@pioneer.edu', '+91-9000000002',
    '17, Tech Park Avenue, Bengaluru, Karnataka - 560001',
    'ENTERPRISE', 'Asia/Kolkata', true, NOW() - INTERVAL '30 days', NOW()
),
(
    '33333333-3333-3333-3333-333333333333',
    'Global College of Arts & Science', 'GCA',
    'admin@globalcollege.edu', '+91-9000000003',
    '8, University Lane, Mumbai, Maharashtra - 400001',
    'BASIC', 'Asia/Kolkata', true, NOW() - INTERVAL '60 days', NOW()
);

-- ────────────────────────────────────────────────────────────
-- 2.  USERS
-- ────────────────────────────────────────────────────────────

-- ── Super Admin
INSERT INTO users (tenant_id, email, password, first_name, last_name, role, enabled, created_at, updated_at) VALUES
(NULL, 'superadmin@campusconnect.io',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy',
 'Super', 'Admin', 'SUPER_ADMIN', true, NOW(), NOW());

-- ── SEC — Admin
INSERT INTO users (tenant_id, email, password, first_name, last_name, role, enabled, created_at, updated_at) VALUES
('11111111-1111-1111-1111-111111111111', 'admin@sunrise.edu',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy',
 'Arjun', 'Sharma', 'ADMIN', true, NOW(), NOW());

-- ── SEC — Faculty (5)
INSERT INTO users (tenant_id, email, password, first_name, last_name, faculty_id, department, phone_number, role, enabled, created_at, updated_at) VALUES
('11111111-1111-1111-1111-111111111111', 'priya.menon@sunrise.edu',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy',
 'Priya', 'Menon', 'FAC001', 'Computer Science', '+91-9000000010', 'FACULTY', true, NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'rahul.verma@sunrise.edu',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy',
 'Rahul', 'Verma', 'FAC002', 'Information Technology', '+91-9000000011', 'FACULTY', true, NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'anita.desai@sunrise.edu',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy',
 'Anita', 'Desai', 'FAC003', 'Mathematics', '+91-9000000012', 'FACULTY', true, NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'suresh.nair@sunrise.edu',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy',
 'Suresh', 'Nair', 'FAC004', 'Electronics', '+91-9000000013', 'FACULTY', true, NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'kavitha.rao@sunrise.edu',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy',
 'Kavitha', 'Rao', 'FAC005', 'Computer Science', '+91-9000000014', 'FACULTY', true, NOW(), NOW());

-- ── SEC — Students (12)
INSERT INTO users (tenant_id, email, password, first_name, last_name, enrollment_number, department, semester, phone_number, role, enabled, created_at, updated_at) VALUES
('11111111-1111-1111-1111-111111111111', 'rahul.student@sunrise.edu',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Rahul',   'Kumar',   'EN2024001', 'Computer Science',       '4', '+91-9100000001', 'STUDENT', true,  NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'sneha.patel@sunrise.edu',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Sneha',   'Patel',   'EN2024002', 'Computer Science',       '4', '+91-9100000002', 'STUDENT', true,  NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'amit.singh@sunrise.edu',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Amit',    'Singh',   'EN2024003', 'Information Technology', '4', '+91-9100000003', 'STUDENT', true,  NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'divya.rao@sunrise.edu',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Divya',   'Rao',     'EN2024004', 'Information Technology', '4', '+91-9100000004', 'STUDENT', true,  NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'kiran.joshi@sunrise.edu',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Kiran',   'Joshi',   'EN2024005', 'Computer Science',       '2', '+91-9100000005', 'STUDENT', true,  NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'pooja.nair@sunrise.edu',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Pooja',   'Nair',    'EN2024006', 'Information Technology', '2', '+91-9100000006', 'STUDENT', true,  NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'vivek.sharma@sunrise.edu',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Vivek',   'Sharma',  'EN2024007', 'Computer Science',       '4', '+91-9100000007', 'STUDENT', true,  NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'meera.iyer@sunrise.edu',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Meera',   'Iyer',    'EN2024008', 'Mathematics',            '4', '+91-9100000008', 'STUDENT', true,  NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'arjun.mehta@sunrise.edu',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Arjun',   'Mehta',   'EN2024009', 'Electronics',            '4', '+91-9100000009', 'STUDENT', true,  NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'neha.gupta@sunrise.edu',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Neha',    'Gupta',   'EN2024010', 'Computer Science',       '6', '+91-9100000010', 'STUDENT', true,  NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'rohit.pillai@sunrise.edu',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Rohit',   'Pillai',  'EN2024011', 'Information Technology', '6', '+91-9100000011', 'STUDENT', false, NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'anjali.kumar@sunrise.edu',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Anjali',  'Kumar',   'EN2024012', 'Electronics',            '2', '+91-9100000012', 'STUDENT', true,  NOW(), NOW());

-- ── PIT — Admin + Faculty (2) + Students (4)
INSERT INTO users (tenant_id, email, password, first_name, last_name, role, enabled, created_at, updated_at) VALUES
('22222222-2222-2222-2222-222222222222', 'admin@pioneer.edu',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy',
 'Vikram', 'Bose', 'ADMIN', true, NOW(), NOW());

INSERT INTO users (tenant_id, email, password, first_name, last_name, faculty_id, department, phone_number, role, enabled, created_at, updated_at) VALUES
('22222222-2222-2222-2222-222222222222', 'deepak.pillai@pioneer.edu',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy',
 'Deepak', 'Pillai', 'PIT-F01', 'Computer Science', '+91-9200000001', 'FACULTY', true, NOW(), NOW()),
('22222222-2222-2222-2222-222222222222', 'sunita.ghosh@pioneer.edu',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy',
 'Sunita', 'Ghosh', 'PIT-F02', 'Data Science', '+91-9200000002', 'FACULTY', true, NOW(), NOW());

INSERT INTO users (tenant_id, email, password, first_name, last_name, enrollment_number, department, semester, phone_number, role, enabled, created_at, updated_at) VALUES
('22222222-2222-2222-2222-222222222222', 'aarav.shah@pioneer.edu',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Aarav',  'Shah',   'PIT2024001', 'Computer Science', '3', '+91-9200000010', 'STUDENT', true, NOW(), NOW()),
('22222222-2222-2222-2222-222222222222', 'ishaan.roy@pioneer.edu',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Ishaan', 'Roy',    'PIT2024002', 'Data Science',     '3', '+91-9200000011', 'STUDENT', true, NOW(), NOW()),
('22222222-2222-2222-2222-222222222222', 'tanvi.mishra@pioneer.edu',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Tanvi',  'Mishra', 'PIT2024003', 'Computer Science', '5', '+91-9200000012', 'STUDENT', true, NOW(), NOW()),
('22222222-2222-2222-2222-222222222222', 'rohan.jain@pioneer.edu',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Rohan',  'Jain',   'PIT2024004', 'Data Science',     '5', '+91-9200000013', 'STUDENT', true, NOW(), NOW());

-- ── GCA — Admin + Faculty (1) + Students (3)
INSERT INTO users (tenant_id, email, password, first_name, last_name, role, enabled, created_at, updated_at) VALUES
('33333333-3333-3333-3333-333333333333', 'admin@globalcollege.edu',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy',
 'Shalini', 'Kapoor', 'ADMIN', true, NOW(), NOW());

INSERT INTO users (tenant_id, email, password, first_name, last_name, faculty_id, department, phone_number, role, enabled, created_at, updated_at) VALUES
('33333333-3333-3333-3333-333333333333', 'james.fernandes@globalcollege.edu',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy',
 'James', 'Fernandes', 'GCA-F01', 'Commerce', '+91-9300000001', 'FACULTY', true, NOW(), NOW());

INSERT INTO users (tenant_id, email, password, first_name, last_name, enrollment_number, department, semester, phone_number, role, enabled, created_at, updated_at) VALUES
('33333333-3333-3333-3333-333333333333', 'prachi.joshi@globalcollege.edu',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Prachi', 'Joshi', 'GCA2024001', 'Commerce', '2', '+91-9300000010', 'STUDENT', true, NOW(), NOW()),
('33333333-3333-3333-3333-333333333333', 'samir.khan@globalcollege.edu',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Samir',  'Khan',  'GCA2024002', 'Commerce', '2', '+91-9300000011', 'STUDENT', true, NOW(), NOW()),
('33333333-3333-3333-3333-333333333333', 'riya.das@globalcollege.edu',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKxoS/Mqy', 'Riya',   'Das',   'GCA2024003', 'Commerce', '4', '+91-9300000012', 'STUDENT', true, NOW(), NOW());

-- ────────────────────────────────────────────────────────────
-- 3.  DEPARTMENTS
-- ────────────────────────────────────────────────────────────

-- SEC
INSERT INTO departments (tenant_id, name, code, head_faculty_id, description, created_at, updated_at) VALUES
('11111111-1111-1111-1111-111111111111', 'Computer Science',       'CS',   'FAC001', 'B.E. Computer Science & Engineering',      NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'Information Technology', 'IT',   'FAC002', 'B.E. Information Technology',              NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'Mathematics',            'MATH', 'FAC003', 'Applied Mathematics Department',            NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'Electronics',            'EC',   'FAC004', 'Electronics & Communication Engineering',   NOW(), NOW()),
('11111111-1111-1111-1111-111111111111', 'Mechanical',             'ME',   NULL,     'Mechanical Engineering Department',         NOW(), NOW());

-- PIT
INSERT INTO departments (tenant_id, name, code, head_faculty_id, description, created_at, updated_at) VALUES
('22222222-2222-2222-2222-222222222222', 'Computer Science', 'CS',  'PIT-F01', 'B.Tech Computer Science',  NOW(), NOW()),
('22222222-2222-2222-2222-222222222222', 'Data Science',     'DS',  'PIT-F02', 'B.Sc. Data Science & AI',  NOW(), NOW());

-- GCA
INSERT INTO departments (tenant_id, name, code, head_faculty_id, description, created_at, updated_at) VALUES
('33333333-3333-3333-3333-333333333333', 'Commerce',    'COM', 'GCA-F01', 'Bachelor of Commerce',             NOW(), NOW()),
('33333333-3333-3333-3333-333333333333', 'Arts',        'ART', NULL,      'Bachelor of Arts',                 NOW(), NOW()),
('33333333-3333-3333-3333-333333333333', 'Science',     'SCI', NULL,      'Bachelor of Science',              NOW(), NOW());

-- ────────────────────────────────────────────────────────────
-- 4.  COURSES
-- ────────────────────────────────────────────────────────────

-- SEC — Sem 2
INSERT INTO courses (tenant_id, course_code, course_name, department, credits, instructor, faculty_id, semester, subject_type, category) VALUES
('11111111-1111-1111-1111-111111111111', 'CS201',  'Programming Fundamentals',          'Computer Science',       4, 'Priya Menon',  'FAC001', '2', 'COMPULSORY', 'Core'),
('11111111-1111-1111-1111-111111111111', 'IT201',  'Digital Logic Design',              'Information Technology', 3, 'Rahul Verma',  'FAC002', '2', 'COMPULSORY', 'Core'),
('11111111-1111-1111-1111-111111111111', 'MATH201','Engineering Mathematics II',        'Mathematics',            4, 'Anita Desai',  'FAC003', '2', 'COMPULSORY', 'Core'),
('11111111-1111-1111-1111-111111111111', 'EC201',  'Basic Electronics',                 'Electronics',            3, 'Suresh Nair',  'FAC004', '2', 'COMPULSORY', 'Core');

-- SEC — Sem 4
INSERT INTO courses (tenant_id, course_code, course_name, department, credits, instructor, faculty_id, semester, subject_type, category) VALUES
('11111111-1111-1111-1111-111111111111', 'CS401',  'Data Structures & Algorithms',      'Computer Science',       4, 'Priya Menon',  'FAC001', '4', 'COMPULSORY', 'Core'),
('11111111-1111-1111-1111-111111111111', 'CS402',  'Operating Systems',                 'Computer Science',       4, 'Kavitha Rao',  'FAC005', '4', 'COMPULSORY', 'Core'),
('11111111-1111-1111-1111-111111111111', 'CS403',  'Database Management Systems',       'Computer Science',       3, 'Rahul Verma',  'FAC002', '4', 'COMPULSORY', 'Core'),
('11111111-1111-1111-1111-111111111111', 'IT401',  'Web Technologies',                  'Information Technology', 4, 'Rahul Verma',  'FAC002', '4', 'COMPULSORY', 'Core'),
('11111111-1111-1111-1111-111111111111', 'IT402',  'Software Engineering',              'Information Technology', 3, 'Priya Menon',  'FAC001', '4', 'COMPULSORY', 'Core'),
('11111111-1111-1111-1111-111111111111', 'MATH401','Engineering Mathematics IV',        'Mathematics',            4, 'Anita Desai',  'FAC003', '4', 'COMPULSORY', 'Core'),
('11111111-1111-1111-1111-111111111111', 'EC401',  'Microprocessors & Microcontrollers','Electronics',            4, 'Suresh Nair',  'FAC004', '4', 'COMPULSORY', 'Core'),
('11111111-1111-1111-1111-111111111111', 'CS404',  'Computer Networks',                 'Computer Science',       3, 'Kavitha Rao',  'FAC005', '4', 'OPTIONAL',   'Elective');

-- SEC — Sem 6
INSERT INTO courses (tenant_id, course_code, course_name, department, credits, instructor, faculty_id, semester, subject_type, category) VALUES
('11111111-1111-1111-1111-111111111111', 'CS601',  'Machine Learning',                  'Computer Science',       4, 'Kavitha Rao',  'FAC005', '6', 'COMPULSORY', 'Core'),
('11111111-1111-1111-1111-111111111111', 'CS602',  'Cloud Computing',                   'Computer Science',       3, 'Priya Menon',  'FAC001', '6', 'OPTIONAL',   'Elective'),
('11111111-1111-1111-1111-111111111111', 'IT601',  'Cyber Security',                    'Information Technology', 4, 'Rahul Verma',  'FAC002', '6', 'COMPULSORY', 'Core'),
('11111111-1111-1111-1111-111111111111', 'CS603',  'Deep Learning',                     'Computer Science',       3, 'Kavitha Rao',  'FAC005', '6', 'OPTIONAL',   'Elective'),
('11111111-1111-1111-1111-111111111111', 'CS604',  'Project Work',                      'Computer Science',       6, 'Priya Menon',  'FAC001', '6', 'COMPULSORY', 'Project');

-- PIT
INSERT INTO courses (tenant_id, course_code, course_name, department, credits, instructor, faculty_id, semester, subject_type, category) VALUES
('22222222-2222-2222-2222-222222222222', 'PIT-CS301', 'Algorithms & Complexity',   'Computer Science', 4, 'Deepak Pillai', 'PIT-F01', '3', 'COMPULSORY', 'Core'),
('22222222-2222-2222-2222-222222222222', 'PIT-DS301', 'Data Analysis with Python', 'Data Science',     4, 'Sunita Ghosh',  'PIT-F02', '3', 'COMPULSORY', 'Core'),
('22222222-2222-2222-2222-222222222222', 'PIT-CS501', 'Artificial Intelligence',   'Computer Science', 4, 'Deepak Pillai', 'PIT-F01', '5', 'COMPULSORY', 'Core'),
('22222222-2222-2222-2222-222222222222', 'PIT-DS501', 'Deep Learning',             'Data Science',     4, 'Sunita Ghosh',  'PIT-F02', '5', 'OPTIONAL',   'Elective');

-- GCA
INSERT INTO courses (tenant_id, course_code, course_name, department, credits, instructor, faculty_id, semester, subject_type, category) VALUES
('33333333-3333-3333-3333-333333333333', 'GCA-COM201', 'Financial Accounting', 'Commerce', 4, 'James Fernandes', 'GCA-F01', '2', 'COMPULSORY', 'Core'),
('33333333-3333-3333-3333-333333333333', 'GCA-COM401', 'Business Statistics',  'Commerce', 3, 'James Fernandes', 'GCA-F01', '4', 'COMPULSORY', 'Core');

-- ────────────────────────────────────────────────────────────
-- 5.  TIMETABLE  (SEC — full week, all semesters)
-- ────────────────────────────────────────────────────────────
INSERT INTO timetable_entries (tenant_id, day_of_week, time_slot, course_code, course_name, faculty_id, faculty_name, room_number, semester) VALUES
-- ── Sem 4 · Monday
('11111111-1111-1111-1111-111111111111','MONDAY','08:00-09:00','CS401','Data Structures & Algorithms','FAC001','Priya Menon','A101','4'),
('11111111-1111-1111-1111-111111111111','MONDAY','09:00-10:00','CS402','Operating Systems','FAC005','Kavitha Rao','A102','4'),
('11111111-1111-1111-1111-111111111111','MONDAY','10:15-11:15','CS403','Database Management Systems','FAC002','Rahul Verma','B201','4'),
('11111111-1111-1111-1111-111111111111','MONDAY','11:15-12:15','MATH401','Engineering Mathematics IV','FAC003','Anita Desai','C301','4'),
('11111111-1111-1111-1111-111111111111','MONDAY','14:00-15:00','IT401','Web Technologies','FAC002','Rahul Verma','A103','4'),
('11111111-1111-1111-1111-111111111111','MONDAY','15:00-16:00','IT402','Software Engineering','FAC001','Priya Menon','A101','4'),
-- ── Sem 4 · Tuesday
('11111111-1111-1111-1111-111111111111','TUESDAY','08:00-09:00','EC401','Microprocessors & Microcontrollers','FAC004','Suresh Nair','LAB1','4'),
('11111111-1111-1111-1111-111111111111','TUESDAY','09:00-10:00','CS401','Data Structures & Algorithms','FAC001','Priya Menon','A101','4'),
('11111111-1111-1111-1111-111111111111','TUESDAY','10:15-11:15','MATH401','Engineering Mathematics IV','FAC003','Anita Desai','C301','4'),
('11111111-1111-1111-1111-111111111111','TUESDAY','11:15-12:15','CS403','Database Management Systems','FAC002','Rahul Verma','B201','4'),
-- ── Sem 4 · Wednesday
('11111111-1111-1111-1111-111111111111','WEDNESDAY','08:00-09:00','IT401','Web Technologies','FAC002','Rahul Verma','A103','4'),
('11111111-1111-1111-1111-111111111111','WEDNESDAY','09:00-10:00','CS402','Operating Systems','FAC005','Kavitha Rao','A102','4'),
('11111111-1111-1111-1111-111111111111','WEDNESDAY','10:15-11:15','CS401','Data Structures & Algorithms','FAC001','Priya Menon','A101','4'),
('11111111-1111-1111-1111-111111111111','WEDNESDAY','14:00-15:00','IT402','Software Engineering','FAC001','Priya Menon','A101','4'),
-- ── Sem 4 · Thursday
('11111111-1111-1111-1111-111111111111','THURSDAY','08:00-09:00','MATH401','Engineering Mathematics IV','FAC003','Anita Desai','C301','4'),
('11111111-1111-1111-1111-111111111111','THURSDAY','09:00-10:00','CS403','Database Management Systems','FAC002','Rahul Verma','B201','4'),
('11111111-1111-1111-1111-111111111111','THURSDAY','10:15-11:15','EC401','Microprocessors & Microcontrollers','FAC004','Suresh Nair','LAB1','4'),
('11111111-1111-1111-1111-111111111111','THURSDAY','14:00-15:00','CS401','Data Structures & Algorithms','FAC001','Priya Menon','A101','4'),
-- ── Sem 4 · Friday
('11111111-1111-1111-1111-111111111111','FRIDAY','08:00-09:00','CS402','Operating Systems','FAC005','Kavitha Rao','A102','4'),
('11111111-1111-1111-1111-111111111111','FRIDAY','09:00-10:00','IT401','Web Technologies','FAC002','Rahul Verma','A103','4'),
('11111111-1111-1111-1111-111111111111','FRIDAY','10:15-11:15','IT402','Software Engineering','FAC001','Priya Menon','A101','4'),
('11111111-1111-1111-1111-111111111111','FRIDAY','11:15-12:15','MATH401','Engineering Mathematics IV','FAC003','Anita Desai','C301','4'),
-- ── Sem 2 · Mon/Wed/Fri
('11111111-1111-1111-1111-111111111111','MONDAY','08:00-09:00','CS201','Programming Fundamentals','FAC001','Priya Menon','B101','2'),
('11111111-1111-1111-1111-111111111111','MONDAY','09:00-10:00','IT201','Digital Logic Design','FAC002','Rahul Verma','B102','2'),
('11111111-1111-1111-1111-111111111111','TUESDAY','08:00-09:00','MATH201','Engineering Mathematics II','FAC003','Anita Desai','C302','2'),
('11111111-1111-1111-1111-111111111111','WEDNESDAY','08:00-09:00','EC201','Basic Electronics','FAC004','Suresh Nair','B103','2'),
('11111111-1111-1111-1111-111111111111','WEDNESDAY','09:00-10:00','CS201','Programming Fundamentals','FAC001','Priya Menon','B101','2'),
('11111111-1111-1111-1111-111111111111','THURSDAY','08:00-09:00','IT201','Digital Logic Design','FAC002','Rahul Verma','B102','2'),
('11111111-1111-1111-1111-111111111111','FRIDAY','08:00-09:00','CS201','Programming Fundamentals','FAC001','Priya Menon','B101','2'),
('11111111-1111-1111-1111-111111111111','FRIDAY','09:00-10:00','MATH201','Engineering Mathematics II','FAC003','Anita Desai','C302','2'),
-- ── Sem 6 · Mon/Wed
('11111111-1111-1111-1111-111111111111','MONDAY','10:15-11:15','CS601','Machine Learning','FAC005','Kavitha Rao','D401','6'),
('11111111-1111-1111-1111-111111111111','MONDAY','11:15-12:15','CS602','Cloud Computing','FAC001','Priya Menon','D402','6'),
('11111111-1111-1111-1111-111111111111','WEDNESDAY','10:15-11:15','IT601','Cyber Security','FAC002','Rahul Verma','D403','6'),
('11111111-1111-1111-1111-111111111111','FRIDAY','10:15-11:15','CS601','Machine Learning','FAC005','Kavitha Rao','D401','6');

-- ────────────────────────────────────────────────────────────
-- 6.  EXAMS  (SEC + PIT)
-- ────────────────────────────────────────────────────────────

-- SEC — Upcoming
INSERT INTO exams (tenant_id, exam_code, course_code, title, description, start_date, end_date, type) VALUES
('11111111-1111-1111-1111-111111111111','EX-CS401-QUIZ1','CS401','DSA Quiz 1','Arrays, Linked Lists and Stacks.',NOW() + INTERVAL '2 days', NOW() + INTERVAL '2 days' + INTERVAL '1 hour','QUIZ'),
('11111111-1111-1111-1111-111111111111','EX-CS401-MID',  'CS401','DSA Midterm Exam','Covers Arrays, Linked Lists, Stacks, Queues and Trees.',NOW() + INTERVAL '7 days', NOW() + INTERVAL '7 days' + INTERVAL '2 hours','MIDTERM'),
('11111111-1111-1111-1111-111111111111','EX-CS402-MID',  'CS402','OS Midterm Exam','Process Management, Memory Management and Scheduling.',NOW() + INTERVAL '9 days', NOW() + INTERVAL '9 days' + INTERVAL '2 hours','MIDTERM'),
('11111111-1111-1111-1111-111111111111','EX-CS403-QUIZ1','CS403','DBMS Quiz 1','ER Diagrams, Normalization and SQL basics.',NOW() + INTERVAL '3 days', NOW() + INTERVAL '3 days' + INTERVAL '1 hour','QUIZ'),
('11111111-1111-1111-1111-111111111111','EX-IT401-MID',  'IT401','Web Tech Midterm','HTML, CSS, JavaScript fundamentals.',NOW() + INTERVAL '8 days', NOW() + INTERVAL '8 days' + INTERVAL '2 hours','MIDTERM'),
('11111111-1111-1111-1111-111111111111','EX-MATH401-MID','MATH401','Maths Midterm','Laplace Transforms and Differential Equations.',NOW() + INTERVAL '12 days', NOW() + INTERVAL '12 days' + INTERVAL '2 hours','MIDTERM'),
('11111111-1111-1111-1111-111111111111','EX-CS601-ASSIGN1','CS601','ML Assignment 1','Implement Linear Regression from scratch in Python.',NOW() + INTERVAL '14 days', NOW() + INTERVAL '20 days','ASSIGNMENT'),
('11111111-1111-1111-1111-111111111111','EX-MATH401-FINAL','MATH401','Engineering Maths Final','Full syllabus.',NOW() + INTERVAL '30 days', NOW() + INTERVAL '30 days' + INTERVAL '3 hours','FINAL'),
('11111111-1111-1111-1111-111111111111','EX-CS401-FINAL', 'CS401','DSA Final Exam','Full syllabus.',NOW() + INTERVAL '45 days', NOW() + INTERVAL '45 days' + INTERVAL '3 hours','FINAL'),
('11111111-1111-1111-1111-111111111111','EX-CS402-FINAL', 'CS402','OS Final Exam','Full syllabus.',NOW() + INTERVAL '47 days', NOW() + INTERVAL '47 days' + INTERVAL '3 hours','FINAL'),
('11111111-1111-1111-1111-111111111111','EX-IT401-FINAL', 'IT401','Web Tech Final','Full syllabus.',NOW() + INTERVAL '49 days', NOW() + INTERVAL '49 days' + INTERVAL '3 hours','FINAL');

-- SEC — Past (for results)
INSERT INTO exams (tenant_id, exam_code, course_code, title, description, start_date, end_date, type) VALUES
('11111111-1111-1111-1111-111111111111','EX-IT401-ASSIGN1','IT401','Web Tech Assignment 1','Build a responsive landing page.',NOW() - INTERVAL '10 days', NOW() - INTERVAL '5 days','ASSIGNMENT'),
('11111111-1111-1111-1111-111111111111','EX-CS403-ASSIGN1','CS403','DBMS Assignment 1','Design and implement a library management DB.',NOW() - INTERVAL '15 days', NOW() - INTERVAL '8 days','ASSIGNMENT'),
('11111111-1111-1111-1111-111111111111','EX-MATH201-QUIZ1','MATH201','Maths II Quiz 1','Integration techniques.',NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days' + INTERVAL '1 hour','QUIZ'),
('11111111-1111-1111-1111-111111111111','EX-CS201-MID','CS201','Programming Fundamentals Midterm','Loops, Functions and Arrays in C.',NOW() - INTERVAL '20 days', NOW() - INTERVAL '20 days' + INTERVAL '2 hours','MIDTERM');

-- PIT
INSERT INTO exams (tenant_id, exam_code, course_code, title, description, start_date, end_date, type) VALUES
('22222222-2222-2222-2222-222222222222','PIT-EX-CS301-MID','PIT-CS301','Algorithms Midterm','Sorting, Searching and Complexity.',NOW() + INTERVAL '5 days', NOW() + INTERVAL '5 days' + INTERVAL '2 hours','MIDTERM'),
('22222222-2222-2222-2222-222222222222','PIT-EX-DS301-ASSIGN1','PIT-DS301','Data Analysis Assignment','EDA on a real-world dataset.',NOW() - INTERVAL '3 days', NOW() + INTERVAL '4 days','ASSIGNMENT');

-- ────────────────────────────────────────────────────────────
-- 7.  RESULTS  (SEC — multiple past exams, all students)
-- ────────────────────────────────────────────────────────────

-- Web Tech Assignment 1
INSERT INTO results (tenant_id, student_id, exam_code, course_code, total_marks, obtained_marks, grade, status, comments, result_date, created_at, updated_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id, 'EX-IT401-ASSIGN1', 'IT401',
       100, m.obtained, m.grade, 'PUBLISHED', m.comment, NOW() - INTERVAL '4 days', NOW(), NOW()
FROM (VALUES
    ('rahul.student@sunrise.edu', 88, 'A',  'Excellent responsive design'),
    ('sneha.patel@sunrise.edu',   92, 'A+', 'Outstanding UI and clean code'),
    ('amit.singh@sunrise.edu',    75, 'B',  'Good work, improve JS interactions'),
    ('divya.rao@sunrise.edu',     81, 'A',  'Well-structured layout'),
    ('kiran.joshi@sunrise.edu',   60, 'C',  'Basic implementation, needs improvement'),
    ('pooja.nair@sunrise.edu',    70, 'B',  'Decent work, add more responsiveness'),
    ('vivek.sharma@sunrise.edu',  85, 'A',  'Clean code and good UI'),
    ('meera.iyer@sunrise.edu',    55, 'C',  'Incomplete CSS styling'),
    ('arjun.mehta@sunrise.edu',   78, 'B+', 'Good work overall'),
    ('neha.gupta@sunrise.edu',    95, 'A+', 'Perfect implementation')
) AS m(email, obtained, grade, comment)
JOIN users u ON u.email = m.email AND u.tenant_id = '11111111-1111-1111-1111-111111111111';

-- DBMS Assignment 1
INSERT INTO results (tenant_id, student_id, exam_code, course_code, total_marks, obtained_marks, grade, status, comments, result_date, created_at, updated_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id, 'EX-CS403-ASSIGN1', 'CS403',
       50, m.obtained, m.grade, 'PUBLISHED', m.comment, NOW() - INTERVAL '7 days', NOW(), NOW()
FROM (VALUES
    ('rahul.student@sunrise.edu', 42, 'A',  'Good ER diagram and normalization'),
    ('sneha.patel@sunrise.edu',   45, 'A+', 'Excellent schema design'),
    ('vivek.sharma@sunrise.edu',  38, 'B',  'Minor normalization issues'),
    ('meera.iyer@sunrise.edu',    30, 'C',  'Needs more detail in ER diagram'),
    ('arjun.mehta@sunrise.edu',   35, 'B-', 'Functional but incomplete'),
    ('neha.gupta@sunrise.edu',    47, 'A+', 'Outstanding database design')
) AS m(email, obtained, grade, comment)
JOIN users u ON u.email = m.email AND u.tenant_id = '11111111-1111-1111-1111-111111111111';

-- Maths II Quiz 1  (Sem 2 students)
INSERT INTO results (tenant_id, student_id, exam_code, course_code, total_marks, obtained_marks, grade, status, comments, result_date, created_at, updated_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id, 'EX-MATH201-QUIZ1', 'MATH201',
       20, m.obtained, m.grade, 'PUBLISHED', m.comment, NOW() - INTERVAL '6 days', NOW(), NOW()
FROM (VALUES
    ('kiran.joshi@sunrise.edu', 16, 'A',  'Good grasp of integration'),
    ('pooja.nair@sunrise.edu',  14, 'B',  'Decent attempt'),
    ('anjali.kumar@sunrise.edu',18, 'A+', 'Perfect score almost!')
) AS m(email, obtained, grade, comment)
JOIN users u ON u.email = m.email AND u.tenant_id = '11111111-1111-1111-1111-111111111111';

-- Programming Fundamentals Midterm
INSERT INTO results (tenant_id, student_id, exam_code, course_code, total_marks, obtained_marks, grade, status, comments, result_date, created_at, updated_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id, 'EX-CS201-MID', 'CS201',
       100, m.obtained, m.grade, 'PUBLISHED', m.comment, NOW() - INTERVAL '18 days', NOW(), NOW()
FROM (VALUES
    ('kiran.joshi@sunrise.edu', 72, 'B+', 'Good understanding of loops'),
    ('pooja.nair@sunrise.edu',  65, 'B',  'Needs to improve function concepts'),
    ('anjali.kumar@sunrise.edu',80, 'A',  'Excellent performance')
) AS m(email, obtained, grade, comment)
JOIN users u ON u.email = m.email AND u.tenant_id = '11111111-1111-1111-1111-111111111111';

-- ────────────────────────────────────────────────────────────
-- 8.  ATTENDANCE  (SEC — multiple courses, 15 working days)
-- ────────────────────────────────────────────────────────────
DO $$
DECLARE
    v_faculty_priya  BIGINT;
    v_faculty_rahul  BIGINT;
    v_faculty_anita  BIGINT;
    v_faculty_kavitha BIGINT;
    rec              RECORD;
    d                DATE;
    day_offset       INT;
    status_val       TEXT;
    status_cycle     TEXT[] := ARRAY['PRESENT','PRESENT','PRESENT','PRESENT','ABSENT','PRESENT','PRESENT','LATE','PRESENT','PRESENT'];
    courses_list     TEXT[] := ARRAY['CS401','CS402','CS403','IT401','IT402','MATH401','CS201','MATH201','EC201','CS601'];
    course_faculty   TEXT[] := ARRAY['FAC001','FAC005','FAC002','FAC002','FAC001','FAC003','FAC001','FAC003','FAC004','FAC005'];
    course_sem       TEXT[] := ARRAY['4','4','4','4','4','4','2','2','2','6'];
    ci               INT;
BEGIN
    SELECT id INTO v_faculty_priya  FROM users WHERE email = 'priya.menon@sunrise.edu';
    SELECT id INTO v_faculty_rahul  FROM users WHERE email = 'rahul.verma@sunrise.edu';
    SELECT id INTO v_faculty_anita  FROM users WHERE email = 'anita.desai@sunrise.edu';
    SELECT id INTO v_faculty_kavitha FROM users WHERE email = 'kavitha.rao@sunrise.edu';

    FOR ci IN 1..array_length(courses_list, 1) LOOP
        FOR rec IN
            SELECT id, semester FROM users
            WHERE tenant_id = '11111111-1111-1111-1111-111111111111'
              AND role = 'STUDENT'
              AND (
                  -- match semester or allow cross-enrollment for electives
                  (course_sem[ci] = '4' AND semester IN ('4','6'))
                  OR (course_sem[ci] = '2' AND semester = '2')
                  OR (course_sem[ci] = '6' AND semester = '6')
              )
        LOOP
            day_offset := 0;
            FOR i IN 0..20 LOOP
                d := CURRENT_DATE - i;
                -- skip weekends
                CONTINUE WHEN EXTRACT(DOW FROM d) IN (0, 6);
                day_offset := day_offset + 1;
                EXIT WHEN day_offset > 15;

                status_val := status_cycle[1 + ((rec.id + i + ci) % 10)];

                INSERT INTO attendance (tenant_id, student_id, course_code, session_date, status, marked_by, remarks, created_at)
                VALUES (
                    '11111111-1111-1111-1111-111111111111',
                    rec.id,
                    courses_list[ci],
                    d,
                    status_val,
                    CASE course_faculty[ci]
                        WHEN 'FAC001' THEN v_faculty_priya
                        WHEN 'FAC002' THEN v_faculty_rahul
                        WHEN 'FAC003' THEN v_faculty_anita
                        ELSE v_faculty_kavitha
                    END,
                    CASE WHEN status_val = 'ABSENT' THEN 'No prior intimation' ELSE NULL END,
                    NOW()
                )
                ON CONFLICT DO NOTHING;
            END LOOP;
        END LOOP;
    END LOOP;
END $$;

-- ────────────────────────────────────────────────────────────
-- 9.  MATERIALS
-- ────────────────────────────────────────────────────────────
INSERT INTO materials (tenant_id, material_code, course_code, title, description, type, uploaded_by, uploaded_date, file_size, download_count, last_updated_date, file_type)
VALUES
-- CS401 — DSA
('11111111-1111-1111-1111-111111111111','MAT-CS401-01','CS401','DSA Lecture Notes - Unit 1','Arrays, Linked Lists and Stacks explained with diagrams.','PDF','Priya Menon',NOW()-INTERVAL '25 days',524288,56,NOW(),'application/pdf'),
('11111111-1111-1111-1111-111111111111','MAT-CS401-02','CS401','DSA Lecture Notes - Unit 2','Trees, Graphs and their traversal algorithms.','PDF','Priya Menon',NOW()-INTERVAL '18 days',618496,42,NOW(),'application/pdf'),
('11111111-1111-1111-1111-111111111111','MAT-CS401-03','CS401','DSA Lecture Notes - Unit 3','Sorting algorithms: Merge Sort, Quick Sort, Heap Sort.','PDF','Priya Menon',NOW()-INTERVAL '10 days',450560,28,NOW(),'application/pdf'),
('11111111-1111-1111-1111-111111111111','MAT-CS401-04','CS401','DSA Practice Problems Set 1','50 solved problems on arrays and linked lists.','DOC','Priya Menon',NOW()-INTERVAL '8 days',204800,31,NOW(),'application/msword'),
('11111111-1111-1111-1111-111111111111','MAT-CS401-05','CS401','DSA Video Lecture Playlist','YouTube playlist link for DSA revision.','LINK','Priya Menon',NOW()-INTERVAL '5 days',0,78,NOW(),'text/uri-list'),
-- CS402 — OS
('11111111-1111-1111-1111-111111111111','MAT-CS402-01','CS402','OS Concepts Slides - Unit 1','Process scheduling and memory management.','PPT','Kavitha Rao',NOW()-INTERVAL '20 days',1048576,35,NOW(),'application/vnd.ms-powerpoint'),
('11111111-1111-1111-1111-111111111111','MAT-CS402-02','CS402','OS Concepts Slides - Unit 2','File Systems, I/O Management and Deadlocks.','PPT','Kavitha Rao',NOW()-INTERVAL '12 days',983040,22,NOW(),'application/vnd.ms-powerpoint'),
('11111111-1111-1111-1111-111111111111','MAT-CS402-03','CS402','OS Previous Year Question Bank','5 years solved OS questions.','PDF','Kavitha Rao',NOW()-INTERVAL '3 days',716800,64,NOW(),'application/pdf'),
-- CS403 — DBMS
('11111111-1111-1111-1111-111111111111','MAT-CS403-01','CS403','DBMS ER Diagram Examples','Complete ER diagram case studies.','PDF','Rahul Verma',NOW()-INTERVAL '16 days',409600,48,NOW(),'application/pdf'),
('11111111-1111-1111-1111-111111111111','MAT-CS403-02','CS403','SQL Query Practice Sheet','100 SQL queries with solutions.','DOC','Rahul Verma',NOW()-INTERVAL '9 days',307200,55,NOW(),'application/msword'),
('11111111-1111-1111-1111-111111111111','MAT-CS403-03','CS403','DBMS Normalization Notes','1NF, 2NF, 3NF and BCNF explained with examples.','PDF','Rahul Verma',NOW()-INTERVAL '4 days',256000,33,NOW(),'application/pdf'),
-- IT401 — Web Tech
('11111111-1111-1111-1111-111111111111','MAT-IT401-01','IT401','HTML & CSS Reference Guide','Complete HTML5 and CSS3 reference.','PDF','Rahul Verma',NOW()-INTERVAL '22 days',307200,72,NOW(),'application/pdf'),
('11111111-1111-1111-1111-111111111111','MAT-IT401-02','IT401','JavaScript ES6 Cheat Sheet','Arrow functions, promises, async/await.','PDF','Rahul Verma',NOW()-INTERVAL '14 days',153600,66,NOW(),'application/pdf'),
('11111111-1111-1111-1111-111111111111','MAT-IT401-03','IT401','React.js Introduction Slides','Component-based architecture and JSX.','PPT','Rahul Verma',NOW()-INTERVAL '6 days',819200,44,NOW(),'application/vnd.ms-powerpoint'),
-- MATH401
('11111111-1111-1111-1111-111111111111','MAT-MATH401-01','MATH401','Laplace Transform Notes','Detailed notes on Laplace and Inverse Laplace.','PDF','Anita Desai',NOW()-INTERVAL '22 days',471040,20,NOW(),'application/pdf'),
('11111111-1111-1111-1111-111111111111','MAT-MATH401-02','MATH401','Engineering Maths Previous Year Papers','5 years solved papers.','PDF','Anita Desai',NOW()-INTERVAL '4 days',716800,85,NOW(),'application/pdf'),
-- CS601 — ML
('11111111-1111-1111-1111-111111111111','MAT-CS601-01','CS601','Machine Learning Lecture Slides','Supervised and Unsupervised Learning.','PPT','Kavitha Rao',NOW()-INTERVAL '10 days',1310720,18,NOW(),'application/vnd.ms-powerpoint'),
('11111111-1111-1111-1111-111111111111','MAT-CS601-02','CS601','Python for ML Cheat Sheet','NumPy, Pandas and Scikit-Learn quick reference.','PDF','Kavitha Rao',NOW()-INTERVAL '5 days',204800,29,NOW(),'application/pdf'),
-- CS201
('11111111-1111-1111-1111-111111111111','MAT-CS201-01','CS201','C Programming Basics Notes','Variables, Loops, Functions and Arrays.','PDF','Priya Menon',NOW()-INTERVAL '30 days',358400,40,NOW(),'application/pdf'),
('11111111-1111-1111-1111-111111111111','MAT-CS201-02','CS201','C Programs Practice Sheet','50 beginner programs with solutions.','DOC','Priya Menon',NOW()-INTERVAL '20 days',204800,38,NOW(),'application/msword');

-- ────────────────────────────────────────────────────────────
-- 10. EVENTS
-- ────────────────────────────────────────────────────────────
INSERT INTO events (tenant_id, title, description, posted_by, created_at, updated_at) VALUES
-- SEC
('11111111-1111-1111-1111-111111111111',
 'Annual Tech Fest — TechXcel 2026',
 'Join us for the biggest technical festival! Events include hackathon, coding contests, robotics, project exhibitions and guest lectures from industry leaders. Prizes worth ₹2 Lakhs. Register at the main office before March 18.',
 'admin@sunrise.edu', NOW()-INTERVAL '3 days', NOW()),
('11111111-1111-1111-1111-111111111111',
 'Campus Placement Drive — TCS & Infosys',
 'TCS and Infosys will conduct an on-campus placement drive on March 28, 2026. Eligible: BE final year students with 60%+ aggregate. Carry 3 copies of resume and ID proof. Report at Main Hall by 8:30 AM.',
 'admin@sunrise.edu', NOW()-INTERVAL '6 days', NOW()),
('11111111-1111-1111-1111-111111111111',
 'Workshop: Introduction to Machine Learning',
 '2-day hands-on workshop on Machine Learning using Python, NumPy and Scikit-Learn. Conducted by Prof. Kavitha Rao. Only 40 seats. Register early at the CS department office.',
 'kavitha.rao@sunrise.edu', NOW()-INTERVAL '2 days', NOW()),
('11111111-1111-1111-1111-111111111111',
 'Sports Day 2026 — Registration Open',
 'Annual Sports Day on March 30, 2026. Events: Cricket, Football, Badminton, Chess and Athletics. Register your team through your class coordinator by March 22.',
 'admin@sunrise.edu', NOW()-INTERVAL '4 days', NOW()),
('11111111-1111-1111-1111-111111111111',
 'Mid-Semester Examination Schedule Released',
 'Mid-semester exams for Semester 4 will be conducted from April 1–10, 2026. Hall tickets available from the examination cell from March 25. Check the timetable section for the full schedule.',
 'admin@sunrise.edu', NOW()-INTERVAL '1 day', NOW()),
('11111111-1111-1111-1111-111111111111',
 'Guest Lecture: Cloud Computing & DevOps',
 'Mr. Rajesh Iyer, Senior Architect at AWS, will deliver a guest lecture on Cloud Computing and DevOps best practices on March 20, 2026 at 2:00 PM in the Seminar Hall. All students are encouraged to attend.',
 'priya.menon@sunrise.edu', NOW(), NOW()),
('11111111-1111-1111-1111-111111111111',
 'Library: New Books Added',
 '50 new titles in Computer Science, Data Structures and Web Development have been added to the college library. Students can issue books from Monday onwards. Check the library catalog for the full list.',
 'admin@sunrise.edu', NOW()-INTERVAL '5 days', NOW()),
('11111111-1111-1111-1111-111111111111',
 'Anti-Ragging Cell — Complaint Portal Launched',
 'The college has launched an online complaint portal for anti-ragging grievances. Students can submit complaints anonymously at the college intranet portal. Zero tolerance policy is strictly enforced.',
 'admin@sunrise.edu', NOW()-INTERVAL '8 days', NOW()),
-- PIT
('22222222-2222-2222-2222-222222222222',
 'Hackathon 2026 — 24-Hour Coding Challenge',
 'Pioneer Institute presents Hackathon 2026! Form teams of 3-4 and solve real-world problems. Prizes: 1st ₹50,000 | 2nd ₹25,000 | 3rd ₹10,000. Registrations open at pioneer.edu/hackathon.',
 'admin@pioneer.edu', NOW()-INTERVAL '2 days', NOW()),
('22222222-2222-2222-2222-222222222222',
 'AI Research Symposium',
 'Annual research symposium on Artificial Intelligence and Data Science. Paper submissions due March 25. Selected papers will be published in the PIT Research Journal.',
 'deepak.pillai@pioneer.edu', NOW()-INTERVAL '5 days', NOW()),
-- GCA
('33333333-3333-3333-3333-333333333333',
 'Commerce Fest — Bizcraft 2026',
 'Inter-college Commerce festival featuring business plan competitions, stock market simulation, and management games. Open for all commerce students. Register before March 20.',
 'admin@globalcollege.edu', NOW()-INTERVAL '1 day', NOW()),
('33333333-3333-3333-3333-333333333333',
 'Scholarship Applications Open',
 'Merit-based scholarship applications for the academic year 2026-27 are now open. Students with 75%+ attendance and 70%+ marks are eligible. Submit applications at the administrative office.',
 'admin@globalcollege.edu', NOW()-INTERVAL '3 days', NOW());

-- ────────────────────────────────────────────────────────────
-- 11. NOTIFICATIONS
-- ────────────────────────────────────────────────────────────

-- Exam reminder → all SEC students
INSERT INTO notifications (tenant_id, user_id, title, message, type, is_read, created_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id,
    'DSA Midterm in 7 Days',
    'DSA Midterm Exam (CS401) is scheduled on ' || TO_CHAR(NOW() + INTERVAL '7 days', 'DD Mon YYYY') || '. Covers Arrays, Linked Lists, Stacks, Queues and Trees. All the best!',
    'EXAM', false, NOW()
FROM users u WHERE u.tenant_id = '11111111-1111-1111-1111-111111111111' AND u.role = 'STUDENT';

-- Quiz reminder → all SEC students
INSERT INTO notifications (tenant_id, user_id, title, message, type, is_read, created_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id,
    'DBMS Quiz Tomorrow',
    'DBMS Quiz 1 (CS403) is scheduled tomorrow. Topics: ER Diagrams and Normalization. Duration: 1 hour.',
    'EXAM', false, NOW() - INTERVAL '2 hours'
FROM users u WHERE u.tenant_id = '11111111-1111-1111-1111-111111111111' AND u.role = 'STUDENT';

-- Result published — IT401 → all SEC students
INSERT INTO notifications (tenant_id, user_id, title, message, type, is_read, created_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id,
    'Result Published — Web Tech Assignment 1',
    'Your result for IT401 Web Technologies Assignment 1 has been published. Login to check your grade.',
    'RESULT', false, NOW() - INTERVAL '4 days'
FROM users u WHERE u.tenant_id = '11111111-1111-1111-1111-111111111111' AND u.role = 'STUDENT';

-- Result published — CS403 → CS students only
INSERT INTO notifications (tenant_id, user_id, title, message, type, is_read, created_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id,
    'Result Published — DBMS Assignment 1',
    'Your result for CS403 Database Management Systems Assignment 1 is now available. Check the Results section.',
    'RESULT', true, NOW() - INTERVAL '7 days'
FROM users u WHERE u.tenant_id = '11111111-1111-1111-1111-111111111111' AND u.role = 'STUDENT' AND u.department = 'Computer Science';

-- Low attendance warning → specific students
INSERT INTO notifications (tenant_id, user_id, title, message, type, is_read, created_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id,
    'Low Attendance Warning ⚠️',
    'Your attendance in one or more subjects has dropped below 75%. Please contact your faculty immediately to avoid being barred from exams.',
    'ATTENDANCE', false, NOW() - INTERVAL '1 day'
FROM users u WHERE u.email IN ('kiran.joshi@sunrise.edu', 'arjun.mehta@sunrise.edu', 'meera.iyer@sunrise.edu')
  AND u.tenant_id = '11111111-1111-1111-1111-111111111111';

-- New material uploaded → all SEC students
INSERT INTO notifications (tenant_id, user_id, title, message, type, is_read, created_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id,
    'New Material: Engineering Maths Previous Year Papers',
    'Prof. Anita Desai uploaded "Engineering Maths Previous Year Papers" for MATH401. Check the Materials section.',
    'GENERAL', true, NOW() - INTERVAL '4 days'
FROM users u WHERE u.tenant_id = '11111111-1111-1111-1111-111111111111' AND u.role = 'STUDENT';

-- Tech Fest event → all SEC users
INSERT INTO notifications (tenant_id, user_id, title, message, type, is_read, created_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id,
    'TechXcel 2026 — Register Now!',
    'Annual Tech Fest registrations are now open. Prizes worth ₹2 Lakhs. Register before March 18.',
    'EVENT', false, NOW() - INTERVAL '3 days'
FROM users u WHERE u.tenant_id = '11111111-1111-1111-1111-111111111111' AND u.role IN ('STUDENT','FACULTY');

-- Placement drive → Sem 6 students
INSERT INTO notifications (tenant_id, user_id, title, message, type, is_read, created_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id,
    'Placement Drive — TCS & Infosys on March 28',
    'TCS and Infosys campus placement drive on March 28. Report to Main Hall by 8:30 AM with resume and ID proof.',
    'GENERAL', false, NOW() - INTERVAL '6 days'
FROM users u WHERE u.tenant_id = '11111111-1111-1111-1111-111111111111' AND u.semester = '6';

-- Guest lecture → all SEC students (already read)
INSERT INTO notifications (tenant_id, user_id, title, message, type, is_read, created_at)
SELECT '11111111-1111-1111-1111-111111111111', u.id,
    'Guest Lecture Today — Cloud Computing & DevOps',
    'Mr. Rajesh Iyer from AWS will speak today at 2:00 PM in the Seminar Hall. Don''t miss it!',
    'EVENT', true, NOW() - INTERVAL '6 hours'
FROM users u WHERE u.tenant_id = '11111111-1111-1111-1111-111111111111' AND u.role = 'STUDENT';

-- PIT notifications
INSERT INTO notifications (tenant_id, user_id, title, message, type, is_read, created_at)
SELECT '22222222-2222-2222-2222-222222222222', u.id,
    'Algorithms Midterm in 5 Days',
    'Algorithms Midterm (PIT-CS301) is on ' || TO_CHAR(NOW() + INTERVAL '5 days', 'DD Mon YYYY') || '. Revise Sorting, Searching and Complexity Analysis.',
    'EXAM', false, NOW()
FROM users u WHERE u.tenant_id = '22222222-2222-2222-2222-222222222222' AND u.role = 'STUDENT';

-- GCA notifications
INSERT INTO notifications (tenant_id, user_id, title, message, type, is_read, created_at)
SELECT '33333333-3333-3333-3333-333333333333', u.id,
    'Scholarship Applications Open',
    'Merit-based scholarship applications for 2026-27 are now open. Eligible if 75%+ attendance and 70%+ marks. Submit at admin office.',
    'GENERAL', false, NOW() - INTERVAL '3 days'
FROM users u WHERE u.tenant_id = '33333333-3333-3333-3333-333333333333' AND u.role = 'STUDENT';

-- ────────────────────────────────────────────────────────────
-- 12. VERIFY
-- ────────────────────────────────────────────────────────────
SELECT
    t."table",
    t.rows
FROM (
    SELECT 'tenants'           AS "table", COUNT(*) AS rows FROM tenants           UNION ALL
    SELECT 'users',             COUNT(*) FROM users                                 UNION ALL
    SELECT 'departments',       COUNT(*) FROM departments                           UNION ALL
    SELECT 'courses',           COUNT(*) FROM courses                               UNION ALL
    SELECT 'timetable_entries', COUNT(*) FROM timetable_entries                     UNION ALL
    SELECT 'exams',             COUNT(*) FROM exams                                 UNION ALL
    SELECT 'results',           COUNT(*) FROM results                               UNION ALL
    SELECT 'attendance',        COUNT(*) FROM attendance                            UNION ALL
    SELECT 'materials',         COUNT(*) FROM materials                             UNION ALL
    SELECT 'events',            COUNT(*) FROM events                                UNION ALL
    SELECT 'notifications',     COUNT(*) FROM notifications
) t
ORDER BY t."table";
