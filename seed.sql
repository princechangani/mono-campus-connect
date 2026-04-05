-- =============================================================================
-- SEED DATA — MonoCampusConnect
-- Tenant: '11111111-0000-0000-0000-000000000001'
-- Generated against backup1.sql schema (pg_dump 2026-03-22)
--
-- Timestamp types per table:
--   WITHOUT time zone: otp_tokens, roles, tenants, user_roles, users
--   WITH    time zone: all other tables
--
-- All passwords are BCrypt of "Password@123"
-- =============================================================================

BEGIN;

-- ---------------------------------------------------------------------------
-- 1. TENANTS  (timestamp WITHOUT time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.tenants (
    tenant_public_id, slug, created_at, created_by,
    deleted_at, deleted_by, domain, is_active, is_deleted,
    locale, logo_url, max_faculty, max_students, name,
    primary_color, subscription_expires_at, plan, tenant_id,
    timezone, updated_at, updated_by
) VALUES (
    '11111111-0000-0000-0000-000000000001',
    'demo-college',
    NOW()::timestamp, NULL,
    NULL::timestamp, NULL,
    'democollege.edu.in', TRUE, FALSE,
    'en', NULL, 100, 2000,
    'Demo Engineering College',
    '#1A73E8',
    '2027-03-31 00:00:00'::timestamp,
    'professional', 1,
    'Asia/Kolkata',
    NOW()::timestamp, NULL
);

-- ---------------------------------------------------------------------------
-- 2. USERS  (timestamp WITHOUT time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.users (
    user_id, address_line1, avatar_url, created_at, created_by,
    date_of_birth, deleted_at, deleted_by, email, email_verified_at,
    is_active, first_name, is_deleted, last_login_at, last_login_ip,
    last_name, locale, mfa_enabled, mfa_secret, password_hash,
    password_reset_expires_at, password_reset_token, phone,
    phone_verified_at, tenant_id, timezone, updated_at, updated_by,
    user_public_id
) VALUES
-- Super Admin
(1, NULL, NULL, NOW()::timestamp, NULL,
 NULL::timestamp, NULL::timestamp, NULL,
 'superadmin@democollege.edu.in', NOW()::timestamp,
 TRUE, 'Super', FALSE, NULL::timestamp, NULL,
 'Admin', 'en', FALSE, NULL,
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LtBomDEG6Qa',
 NULL::timestamp, NULL, '9000000001',
 NULL::timestamp, '11111111-0000-0000-0000-000000000001',
 'Asia/Kolkata', NOW()::timestamp, NULL,
 'aaaaaaaa-0000-0000-0000-000000000001'),

-- Admin
(2, NULL, NULL, NOW()::timestamp, 1,
 NULL::timestamp, NULL::timestamp, NULL,
 'admin@democollege.edu.in', NOW()::timestamp,
 TRUE, 'College', FALSE, NULL::timestamp, NULL,
 'Admin', 'en', FALSE, NULL,
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LtBomDEG6Qa',
 NULL::timestamp, NULL, '9000000002',
 NULL::timestamp, '11111111-0000-0000-0000-000000000001',
 'Asia/Kolkata', NOW()::timestamp, 1,
 'aaaaaaaa-0000-0000-0000-000000000002'),

-- Faculty HOD
(3, '12, Navrangpura', NULL, NOW()::timestamp, 1,
 '1978-05-15 00:00:00'::timestamp, NULL::timestamp, NULL,
 'rajesh.sharma@democollege.edu.in', NOW()::timestamp,
 TRUE, 'Rajesh', FALSE, NULL::timestamp, NULL,
 'Sharma', 'en', FALSE, NULL,
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LtBomDEG6Qa',
 NULL::timestamp, NULL, '9111111001',
 NULL::timestamp, '11111111-0000-0000-0000-000000000001',
 'Asia/Kolkata', NOW()::timestamp, 1,
 'aaaaaaaa-0000-0000-0000-000000000003'),

-- Faculty Assistant Professor
(4, '45, Satellite Road', NULL, NOW()::timestamp, 1,
 '1990-08-22 00:00:00'::timestamp, NULL::timestamp, NULL,
 'priya.mehta@democollege.edu.in', NOW()::timestamp,
 TRUE, 'Priya', FALSE, NULL::timestamp, NULL,
 'Mehta', 'en', FALSE, NULL,
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LtBomDEG6Qa',
 NULL::timestamp, NULL, '9111111002',
 NULL::timestamp, '11111111-0000-0000-0000-000000000001',
 'Asia/Kolkata', NOW()::timestamp, 1,
 'aaaaaaaa-0000-0000-0000-000000000004'),

-- Student 1
(5, '7, Maninagar Society', NULL, NOW()::timestamp, 1,
 '2005-03-10 00:00:00'::timestamp, NULL::timestamp, NULL,
 'arjun.patel@student.democollege.edu.in', NOW()::timestamp,
 TRUE, 'Arjun', FALSE, NULL::timestamp, NULL,
 'Patel', 'en', FALSE, NULL,
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LtBomDEG6Qa',
 NULL::timestamp, NULL, '9222222001',
 NULL::timestamp, '11111111-0000-0000-0000-000000000001',
 'Asia/Kolkata', NOW()::timestamp, 1,
 'aaaaaaaa-0000-0000-0000-000000000005'),

-- Student 2
(6, '23, Bopal Residency', NULL, NOW()::timestamp, 1,
 '2005-11-25 00:00:00'::timestamp, NULL::timestamp, NULL,
 'sneha.joshi@student.democollege.edu.in', NOW()::timestamp,
 TRUE, 'Sneha', FALSE, NULL::timestamp, NULL,
 'Joshi', 'en', FALSE, NULL,
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LtBomDEG6Qa',
 NULL::timestamp, NULL, '9222222002',
 NULL::timestamp, '11111111-0000-0000-0000-000000000001',
 'Asia/Kolkata', NOW()::timestamp, 1,
 'aaaaaaaa-0000-0000-0000-000000000006');

SELECT setval('public.users_user_id_seq', 6, true);

-- ---------------------------------------------------------------------------
-- 3. USER_ROLES  (timestamp WITHOUT time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.user_roles (
    user_role_id, assigned_at, context_id, context_type,
    created_at, created_by, deleted_at, deleted_by, expires_at,
    is_deleted, tenant_id, updated_at, updated_by,
    user_role_public_id, role_id, user_id
) VALUES
(1, NOW()::timestamp, NULL, NULL, NOW()::timestamp, NULL,
 NULL::timestamp, NULL, NULL::timestamp, FALSE,
 '11111111-0000-0000-0000-000000000001', NOW()::timestamp, NULL,
 'bbbbbbbb-0000-0000-0000-000000000001', 1, 1),

(2, NOW()::timestamp, NULL, NULL, NOW()::timestamp, 1,
 NULL::timestamp, NULL, NULL::timestamp, FALSE,
 '11111111-0000-0000-0000-000000000001', NOW()::timestamp, 1,
 'bbbbbbbb-0000-0000-0000-000000000002', 2, 2),

(3, NOW()::timestamp, NULL, NULL, NOW()::timestamp, 1,
 NULL::timestamp, NULL, NULL::timestamp, FALSE,
 '11111111-0000-0000-0000-000000000001', NOW()::timestamp, 1,
 'bbbbbbbb-0000-0000-0000-000000000003', 3, 3),

(4, NOW()::timestamp, NULL, NULL, NOW()::timestamp, 1,
 NULL::timestamp, NULL, NULL::timestamp, FALSE,
 '11111111-0000-0000-0000-000000000001', NOW()::timestamp, 1,
 'bbbbbbbb-0000-0000-0000-000000000004', 4, 4),

(5, NOW()::timestamp, NULL, NULL, NOW()::timestamp, 1,
 NULL::timestamp, NULL, NULL::timestamp, FALSE,
 '11111111-0000-0000-0000-000000000001', NOW()::timestamp, 1,
 'bbbbbbbb-0000-0000-0000-000000000005', 5, 5),

(6, NOW()::timestamp, NULL, NULL, NOW()::timestamp, 1,
 NULL::timestamp, NULL, NULL::timestamp, FALSE,
 '11111111-0000-0000-0000-000000000001', NOW()::timestamp, 1,
 'bbbbbbbb-0000-0000-0000-000000000006', 5, 6);

SELECT setval('public.user_roles_user_role_id_seq', 6, true);

-- ---------------------------------------------------------------------------
-- 4. DEPARTMENTS  (timestamp WITH time zone — plain NOW() / NULL is fine)
-- ---------------------------------------------------------------------------
INSERT INTO public.departments (
    department_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, code, department_public_id,
    description, head_faculty_id, name, parent_department_id, tenant_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'CS', 'cccccccc-0000-0000-0000-000000000001',
 'Department of Computer Science & Engineering',
 NULL,   -- updated after faculty insert below
 'Computer Science & Engineering', NULL,
 '11111111-0000-0000-0000-000000000001'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'ME', 'cccccccc-0000-0000-0000-000000000002',
 'Department of Mechanical Engineering',
 NULL, 'Mechanical Engineering', NULL,
 '11111111-0000-0000-0000-000000000001');

SELECT setval('public.departments_department_id_seq', 2, true);

-- ---------------------------------------------------------------------------
-- 5. ACADEMIC YEARS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.academic_years (
    academic_year_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, academic_year_public_id,
    end_date, is_current, label, start_date, tenant_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'eeeeeeee-0000-0000-0000-000000000001',
 '2024-06-30', FALSE, '2023-24', '2023-07-01',
 '11111111-0000-0000-0000-000000000001'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'eeeeeeee-0000-0000-0000-000000000002',
 '2025-06-30', TRUE, '2024-25', '2024-07-01',
 '11111111-0000-0000-0000-000000000001');

SELECT setval('public.academic_years_academic_year_id_seq', 2, true);

-- ---------------------------------------------------------------------------
-- 6. PROGRAMS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.programs (
    program_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, code, degree_level,
    department_id, duration_years, is_active, name,
    program_public_id, tenant_id, total_credits, total_semesters
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'BTCS', 'UG', 1, 4, TRUE, 'B.Tech Computer Science',
 'ffffffff-0000-0000-0000-000000000001',
 '11111111-0000-0000-0000-000000000001', 160, 8),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'MTCS', 'PG', 1, 2, TRUE, 'M.Tech Computer Science',
 'ffffffff-0000-0000-0000-000000000002',
 '11111111-0000-0000-0000-000000000001', 80, 4),

(3, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'BTME', 'UG', 2, 4, TRUE, 'B.Tech Mechanical Engineering',
 'ffffffff-0000-0000-0000-000000000003',
 '11111111-0000-0000-0000-000000000001', 160, 8);

SELECT setval('public.programs_program_id_seq', 3, true);

-- ---------------------------------------------------------------------------
-- 7. COURSES  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.courses (
    course_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, code, course_public_id,
    course_type, credits, department_id, description, is_active,
    lab_hours_per_week, lecture_hours_per_week, name, tenant_id,
    tutorial_hours_per_week, semester_number, category, subject_type,
    instructor_name
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'CS201', '10000000-0000-0000-0000-000000000001',
 'theory', 4, 1, 'Fundamentals of data structures and algorithm design.', TRUE,
 2, 3, 'Data Structures & Algorithms',
 '11111111-0000-0000-0000-000000000001', 1, 2, 'Core', 'COMPULSORY',
 'Dr. Rajesh Sharma'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'CS301', '10000000-0000-0000-0000-000000000002',
 'theory', 4, 1, 'Relational databases, SQL, and database design.', TRUE,
 2, 3, 'Database Management Systems',
 '11111111-0000-0000-0000-000000000001', 1, 3, 'Core', 'COMPULSORY',
 'Dr. Rajesh Sharma'),

(3, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'CS302', '10000000-0000-0000-0000-000000000003',
 'theory', 3, 1, 'Process management, memory, and file systems.', TRUE,
 0, 3, 'Operating Systems',
 '11111111-0000-0000-0000-000000000001', 0, 3, 'Core', 'COMPULSORY',
 'Prof. Priya Mehta'),

(4, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'CS401', '10000000-0000-0000-0000-000000000004',
 'theory', 4, 1, 'Networking protocols, TCP/IP, and security.', TRUE,
 2, 3, 'Computer Networks',
 '11111111-0000-0000-0000-000000000001', 1, 4, 'Core', 'COMPULSORY',
 'Prof. Priya Mehta'),

(5, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'CS501', '10000000-0000-0000-0000-000000000005',
 'elective', 3, 1, 'Advanced algorithm design and complexity theory.', TRUE,
 0, 3, 'Advanced Algorithms',
 '11111111-0000-0000-0000-000000000001', 0, 5, 'Elective', 'OPTIONAL',
 'Dr. Rajesh Sharma');

SELECT setval('public.courses_course_id_seq', 5, true);

-- ---------------------------------------------------------------------------
-- 8. PROGRAM_COURSES  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.program_courses (
    program_course_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, course_id, is_elective,
    is_mandatory, program_course_public_id, program_id,
    semester_number, tenant_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 1, FALSE, TRUE,
 '20000000-0000-0000-0000-000000000001', 1, 2,
 '11111111-0000-0000-0000-000000000001'),
(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 2, FALSE, TRUE,
 '20000000-0000-0000-0000-000000000002', 1, 3,
 '11111111-0000-0000-0000-000000000001'),
(3, NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 3, FALSE, TRUE,
 '20000000-0000-0000-0000-000000000003', 1, 3,
 '11111111-0000-0000-0000-000000000001'),
(4, NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 4, FALSE, TRUE,
 '20000000-0000-0000-0000-000000000004', 1, 4,
 '11111111-0000-0000-0000-000000000001'),
(5, NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 5, TRUE, FALSE,
 '20000000-0000-0000-0000-000000000005', 1, 5,
 '11111111-0000-0000-0000-000000000001');

SELECT setval('public.program_courses_program_course_id_seq', 5, true);

-- ---------------------------------------------------------------------------
-- 9. FACULTY  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.faculty (
    faculty_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, department_id, designation,
    employee_id, employment_type, faculty_public_id, joining_date,
    status, tenant_id, user_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 1, 'Professor', 'EMP001', 'permanent',
 '40000000-0000-0000-0000-000000000001',
 '2010-07-15', 'active',
 '11111111-0000-0000-0000-000000000001', 3),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 1, 'Assistant Professor', 'EMP002', 'permanent',
 '40000000-0000-0000-0000-000000000002',
 '2018-07-01', 'active',
 '11111111-0000-0000-0000-000000000001', 4);

SELECT setval('public.faculty_faculty_id_seq', 2, true);

-- Set CS department HOD
UPDATE public.departments SET head_faculty_id = 1 WHERE department_id = 1;

-- ---------------------------------------------------------------------------
-- 10. FACULTY_EDUCATION  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.faculty_education (
    faculty_education_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, degree, faculty_education_public_id,
    faculty_id, field_of_study, grade_or_percentage, institution,
    passing_year, tenant_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'Ph.D', '50000000-0000-0000-0000-000000000001',
 1, 'Computer Science', NULL, 'IIT Bombay', 2008,
 '11111111-0000-0000-0000-000000000001'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'M.Tech', '50000000-0000-0000-0000-000000000002',
 1, 'Computer Science', '9.1 CGPA', 'IIT Bombay', 2003,
 '11111111-0000-0000-0000-000000000001'),

(3, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'M.Tech', '50000000-0000-0000-0000-000000000003',
 2, 'Information Technology', '8.5 CGPA', 'VJTI Mumbai', 2016,
 '11111111-0000-0000-0000-000000000001');

SELECT setval('public.faculty_education_faculty_education_id_seq', 3, true);

-- ---------------------------------------------------------------------------
-- 11. FACULTY_WORK_EXPERIENCE  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.faculty_work_experience (
    work_experience_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, description, faculty_id,
    from_date, organization, role, tenant_id, to_date,
    work_experience_public_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'Software development for enterprise clients.',
 2, '2016-08-01', 'TCS Innovations', 'Software Engineer',
 '11111111-0000-0000-0000-000000000001', '2018-06-30',
 '60000000-0000-0000-0000-000000000001');

SELECT setval('public.faculty_work_experience_work_experience_id_seq', 1, true);

-- ---------------------------------------------------------------------------
-- 12. BATCHES  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.batches (
    batch_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, academic_year_id,
    batch_public_id, class_teacher_id, current_semester,
    max_students, name, program_id, tenant_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 2, '30000000-0000-0000-0000-000000000001',
 2, 1, 60, 'CS-2024', 1,
 '11111111-0000-0000-0000-000000000001'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 1, '30000000-0000-0000-0000-000000000002',
 2, 3, 60, 'CS-2023', 1,
 '11111111-0000-0000-0000-000000000001'),

(3, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 2, '30000000-0000-0000-0000-000000000003',
 NULL, 1, 60, 'ME-2024', 3,
 '11111111-0000-0000-0000-000000000001');

SELECT setval('public.batches_batch_id_seq', 3, true);

-- ---------------------------------------------------------------------------
-- 13. STUDENTS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.students (
    student_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, admission_type, batch_id,
    current_semester, enrollment_date, enrollment_number, status,
    student_public_id, tenant_id, user_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'regular', 1, 1, '2024-07-10', '24CS001', 'active',
 '70000000-0000-0000-0000-000000000001',
 '11111111-0000-0000-0000-000000000001', 5),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'regular', 1, 1, '2024-07-10', '24CS002', 'active',
 '70000000-0000-0000-0000-000000000002',
 '11111111-0000-0000-0000-000000000001', 6);

SELECT setval('public.students_student_id_seq', 2, true);

-- ---------------------------------------------------------------------------
-- 14. ROOMS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.rooms (
    room_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, building, capacity,
    floor, name, room_number, room_public_id, room_type, tenant_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'A Block', 60, 1, 'Classroom A101', 'A101',
 '90000000-0000-0000-0000-000000000001', 'classroom',
 '11111111-0000-0000-0000-000000000001'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'A Block', 60, 1, 'Classroom A102', 'A102',
 '90000000-0000-0000-0000-000000000002', 'classroom',
 '11111111-0000-0000-0000-000000000001'),

(3, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'B Block', 30, 0, 'CS Lab 1', 'B001',
 '90000000-0000-0000-0000-000000000003', 'lab',
 '11111111-0000-0000-0000-000000000001'),

(4, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'C Block', 150, 2, 'Seminar Hall', 'C201',
 '90000000-0000-0000-0000-000000000004', 'seminar',
 '11111111-0000-0000-0000-000000000001');

SELECT setval('public.rooms_room_id_seq', 4, true);

-- ---------------------------------------------------------------------------
-- 15. COURSE_ASSIGNMENTS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.course_assignments (
    course_assignment_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, academic_year_id, batch_id,
    course_assignment_public_id, course_id, faculty_id, is_primary,
    section, semester_number, tenant_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 1, 2, '80000000-0000-0000-0000-000000000001',
 2, 1, TRUE, 'A', 3,
 '11111111-0000-0000-0000-000000000001'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 1, 2, '80000000-0000-0000-0000-000000000002',
 3, 2, TRUE, 'A', 3,
 '11111111-0000-0000-0000-000000000001');

SELECT setval('public.course_assignments_course_assignment_id_seq', 2, true);

-- ---------------------------------------------------------------------------
-- 16. TIMETABLE_SLOTS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.timetable_slots (
    slot_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, course_assignment_id,
    course_id, day_of_week, day_of_week_label, effective_from, effective_to,
    end_time, recurrence, room_id, slot_public_id, slot_type, start_time,
    time_slot, semester_number, course_name, faculty_name, faculty_code,
    room_number, tenant_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 1, 2, 1, 'MONDAY', '2024-07-15', NULL, '10:00:00', 'weekly',
 1, 'a0000000-0000-0000-0000-000000000001', 'lecture', '09:00:00',
 '09:00-10:00', '3', 'Database Management Systems', 'Dr. Rajesh Sharma',
 'EMP001', 'A101', '11111111-0000-0000-0000-000000000001'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 1, 2, 3, 'WEDNESDAY', '2024-07-15', NULL, '10:00:00', 'weekly',
 1, 'a0000000-0000-0000-0000-000000000002', 'lecture', '09:00:00',
 '09:00-10:00', '3', 'Database Management Systems', 'Dr. Rajesh Sharma',
 'EMP001', 'A101', '11111111-0000-0000-0000-000000000001'),

(3, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 2, 3, 2, 'TUESDAY', '2024-07-15', NULL, '11:00:00', 'weekly',
 2, 'a0000000-0000-0000-0000-000000000003', 'lecture', '10:00:00',
 '10:00-11:00', '3', 'Operating Systems', 'Prof. Priya Mehta',
 'EMP002', 'B201', '11111111-0000-0000-0000-000000000001'),

(4, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 2, 3, 4, 'THURSDAY', '2024-07-15', NULL, '11:00:00', 'weekly',
 2, 'a0000000-0000-0000-0000-000000000004', 'lecture', '10:00:00',
 '10:00-11:00', '3', 'Operating Systems', 'Prof. Priya Mehta',
 'EMP002', 'B201', '11111111-0000-0000-0000-000000000001');

SELECT setval('public.timetable_slots_slot_id_seq', 4, true);

-- ---------------------------------------------------------------------------
-- 17. ATTENDANCE_SESSIONS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.attendance_sessions (
    session_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, conducted_by,
    course_assignment_id, end_time, room_id, session_date,
    session_public_id, session_type, slot_id, start_time,
    tenant_id, topic_covered
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 1, 1, '10:00:00', 1, '2024-08-05',
 'b0000000-0000-0000-0000-000000000001',
 'lecture', 1, '09:00:00',
 '11111111-0000-0000-0000-000000000001',
 'Introduction to Relational Model'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 1, 1, '10:00:00', 1, '2024-08-12',
 'b0000000-0000-0000-0000-000000000002',
 'lecture', 1, '09:00:00',
 '11111111-0000-0000-0000-000000000001',
 'ER Diagrams and Normalization'),

(3, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 2, 2, '11:00:00', 2, '2024-08-06',
 'b0000000-0000-0000-0000-000000000003',
 'lecture', 3, '10:00:00',
 '11111111-0000-0000-0000-000000000001',
 'Introduction to Operating Systems');

SELECT setval('public.attendance_sessions_session_id_seq', 3, true);

-- ---------------------------------------------------------------------------
-- 18. ATTENDANCE_RECORDS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.attendance_records (
    attendance_record_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, attendance_record_public_id,
    marked_at, marked_by, remarks, session_id, course_id, session_date,
    status, student_id, tenant_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'c0000000-0000-0000-0000-000000000001',
 NOW(), 1, NULL, 1, 2, '2024-08-05', 'PRESENT', 1,
 '11111111-0000-0000-0000-000000000001'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'c0000000-0000-0000-0000-000000000002',
 NOW(), 1, NULL, 1, 2, '2024-08-05', 'ABSENT', 2,
 '11111111-0000-0000-0000-000000000001'),

(3, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'c0000000-0000-0000-0000-000000000003',
 NOW(), 1, NULL, 2, 2, '2024-08-12', 'PRESENT', 1,
 '11111111-0000-0000-0000-000000000001'),

(4, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'c0000000-0000-0000-0000-000000000004',
 NOW(), 1, NULL, 2, 2, '2024-08-12', 'PRESENT', 2,
 '11111111-0000-0000-0000-000000000001'),

(5, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'c0000000-0000-0000-0000-000000000005',
 NOW(), 2, NULL, 3, 3, '2024-08-06', 'PRESENT', 1,
 '11111111-0000-0000-0000-000000000001'),

(6, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'c0000000-0000-0000-0000-000000000006',
 NOW(), 2, NULL, 3, 3, '2024-08-06', 'PRESENT', 2,
 '11111111-0000-0000-0000-000000000001');

SELECT setval('public.attendance_records_attendance_record_id_seq', 6, true);

-- ---------------------------------------------------------------------------
-- 19. EXAMS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.exams (
    exam_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, academic_year_id, batch_id,
    course_id, exam_code, description, enrolled_student_ids, end_date,
    exam_public_id, exam_type, name, passing_marks, semester_number,
    start_date, tenant_id, total_marks
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 1, 2, 2, 'MID-CS301-2023',
 'Mid-semester assessment for CS301', NULL, '2023-09-25',
 'd0000000-0000-0000-0000-000000000001',
 'MIDTERM', 'Mid-Semester I', 12.00, 3, '2023-09-20',
 '11111111-0000-0000-0000-000000000001', 30.00),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 1, 2, 3, 'END-CS302-2023',
 'End-semester assessment for CS302', NULL, '2023-11-30',
 'd0000000-0000-0000-0000-000000000002',
 'FINAL', 'End-Semester I', 40.00, 3, '2023-11-20',
 '11111111-0000-0000-0000-000000000001', 100.00);

SELECT setval('public.exams_exam_id_seq', 2, true);

-- ---------------------------------------------------------------------------
-- 20. EXAM_SCHEDULES  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.exam_schedules (
    exam_schedule_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, course_id, end_time,
    exam_date, exam_id, exam_schedule_public_id, invigilator_id,
    room_id, start_time, tenant_id
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 2, '12:00:00', '2023-09-21', 1,
 'e0000000-0000-0000-0000-000000000001',
 2, 1, '10:00:00',
 '11111111-0000-0000-0000-000000000001'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 3, '12:00:00', '2023-09-22', 1,
 'e0000000-0000-0000-0000-000000000002',
 1, 1, '10:00:00',
 '11111111-0000-0000-0000-000000000001');

SELECT setval('public.exam_schedules_exam_schedule_id_seq', 2, true);

-- ---------------------------------------------------------------------------
-- 21. MARKS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.marks (
    mark_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, course_id, entered_by,
    exam_id, grade, grade_points, is_absent, is_withheld,
    mark_public_id, marks_obtained, remarks, status, student_id,
    tenant_id, total_marks
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 2, 1, 1, 'A', 9.00, FALSE, FALSE,
 'f0000000-0000-0000-0000-000000000001',
 24.50, NULL, 'PUBLISHED', 1,
 '11111111-0000-0000-0000-000000000001', 30.00),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 2, 1, 1, 'B', 7.00, FALSE, FALSE,
 'f0000000-0000-0000-0000-000000000002',
 19.00, NULL, 'PUBLISHED', 2,
 '11111111-0000-0000-0000-000000000001', 30.00),

(3, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 3, 1, 1, 'A+', 10.00, FALSE, FALSE,
 'f0000000-0000-0000-0000-000000000003',
 28.00, NULL, 'PUBLISHED', 1,
 '11111111-0000-0000-0000-000000000001', 30.00),

(4, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 3, 1, 1, 'B+', 8.00, FALSE, FALSE,
 'f0000000-0000-0000-0000-000000000004',
 22.00, NULL, 'PUBLISHED', 2,
 '11111111-0000-0000-0000-000000000001', 30.00);

SELECT setval('public.marks_mark_id_seq', 4, true);

-- ---------------------------------------------------------------------------
-- 22. FEE_STRUCTURES  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.fee_structures (
    fee_structure_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, academic_year_id, category,
    due_date, fee_name, fee_structure_public_id, late_fee_per_day,
    program_id, semester_number, tenant_id, total_amount
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 2, NULL, '2024-08-31', 'Tuition Fee — Sem 1',
 'fe000000-0000-0000-0000-000000000001',
 50.00, 1, 1,
 '11111111-0000-0000-0000-000000000001', 45000.00),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 2, NULL, '2024-08-31', 'Development Fee — Sem 1',
 'fe000000-0000-0000-0000-000000000002',
 0.00, 1, 1,
 '11111111-0000-0000-0000-000000000001', 5000.00);

SELECT setval('public.fee_structures_fee_structure_id_seq', 2, true);

-- ---------------------------------------------------------------------------
-- 23. FEE_INVOICES  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.fee_invoices (
    invoice_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, discount, due_date,
    fee_structure_id, invoice_number, invoice_public_id, issue_date,
    late_fee, status, student_id, subtotal, tenant_id, total_amount
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 0.00, '2024-08-31', 1,
 'INV-2024-00001',
 'b1000000-0000-0000-0000-000000000001',
 '2024-07-15', 0.00, 'paid', 1, 45000.00,
 '11111111-0000-0000-0000-000000000001', 45000.00),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 0.00, '2024-08-31', 2,
 'INV-2024-00002',
 'b1000000-0000-0000-0000-000000000002',
 '2024-07-15', 0.00, 'sent', 2, 5000.00,
 '11111111-0000-0000-0000-000000000001', 5000.00);

SELECT setval('public.fee_invoices_invoice_id_seq', 2, true);

-- ---------------------------------------------------------------------------
-- 24. FEE_PAYMENTS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.fee_payments (
    payment_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, amount_paid, collected_by,
    gateway_response, invoice_id, payment_date, payment_mode,
    payment_public_id, receipt_number, status, student_id,
    tenant_id, transaction_reference
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 45000.00, NULL, NULL, 1, '2024-08-28', 'upi',
 'b2000000-0000-0000-0000-000000000001',
 'REC-2024-00001', 'success', 1,
 '11111111-0000-0000-0000-000000000001',
 'UPI123456789');

SELECT setval('public.fee_payments_payment_id_seq', 1, true);

-- ---------------------------------------------------------------------------
-- 25. HOLIDAYS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.holidays (
    holiday_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, applies_to, description,
    holiday_date, holiday_public_id, holiday_type, tenant_id, title
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'all', NULL, '2025-01-26',
 'b3000000-0000-0000-0000-000000000001',
 'national', '11111111-0000-0000-0000-000000000001', 'Republic Day'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'all', NULL, '2025-08-15',
 'b3000000-0000-0000-0000-000000000002',
 'national', '11111111-0000-0000-0000-000000000001', 'Independence Day'),

(3, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'all', NULL, '2025-10-20',
 'b3000000-0000-0000-0000-000000000003',
 'state', '11111111-0000-0000-0000-000000000001', 'Diwali'),

(4, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'all', NULL, '2025-09-05',
 'b3000000-0000-0000-0000-000000000004',
 'college', '11111111-0000-0000-0000-000000000001', 'College Foundation Day');

SELECT setval('public.holidays_holiday_id_seq', 4, true);

-- ---------------------------------------------------------------------------
-- 26. ANNOUNCEMENTS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.announcements (
    announcement_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, announcement_public_id,
    audience, audience_ref_id, content, posted_by, expires_at, pinned_until,
    priority, published_at, tenant_id, title
) VALUES
(1, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'b4000000-0000-0000-0000-000000000001',
 'students', NULL,
 'Mid-semester examination schedule for Semester 3 has been published. Check the timetable section.',
 'admin@democollege.edu.in',
 NULL, NULL, 'high', NOW(),
 '11111111-0000-0000-0000-000000000001',
 'Mid-Semester Examination Schedule Released'),

(2, NOW(), 1, NULL, NULL, FALSE, NOW(), 1,
 'b4000000-0000-0000-0000-000000000002',
 'faculty', NULL,
 'All faculty are requested to attend the Faculty Development Programme on 10 Sept 2024 in Seminar Hall C201.',
 'admin@democollege.edu.in',
 NULL, NULL, 'normal', NOW(),
 '11111111-0000-0000-0000-000000000001',
 'Faculty Development Programme — 10 Sept 2024');

SELECT setval('public.announcements_announcement_id_seq', 2, true);

-- ---------------------------------------------------------------------------
-- 27. LEAVE_APPLICATIONS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.leave_applications (
    leave_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, applicant_type, applicant_user_id,
    document_url, from_date, leave_public_id, leave_type, reason,
    reviewed_at, reviewed_by, reviewer_remarks, status, tenant_id, to_date
) VALUES
(1, NOW(), 5, NULL, NULL, FALSE, NOW(), 5,
 'student', 5, NULL, '2024-09-10',
 'b5000000-0000-0000-0000-000000000001',
 'medical', 'Viral fever',
 NOW(), 1, 'Approved with medical certificate.',
 'approved', '11111111-0000-0000-0000-000000000001', '2024-09-12'),

(2, NOW(), 4, NULL, NULL, FALSE, NOW(), 4,
 'faculty', 4, NULL, '2024-10-05',
 'b5000000-0000-0000-0000-000000000002',
 'personal', 'Family function',
 NULL, NULL, NULL,
 'pending', '11111111-0000-0000-0000-000000000001', '2024-10-06');

SELECT setval('public.leave_applications_leave_id_seq', 2, true);

-- ---------------------------------------------------------------------------
-- 28. PERMISSIONS  (WITH time zone)
-- ---------------------------------------------------------------------------
INSERT INTO public.permissions (
    permission_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, action, description,
    module, permission_public_id, resource, tenant_id
) VALUES
(1,  NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'READ',   'View students',        'STUDENT',      gen_random_uuid(), 'students',      '11111111-0000-0000-0000-000000000001'),
(2,  NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'WRITE',  'Create/edit students', 'STUDENT',      gen_random_uuid(), 'students',      '11111111-0000-0000-0000-000000000001'),
(3,  NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'DELETE', 'Delete students',      'STUDENT',      gen_random_uuid(), 'students',      '11111111-0000-0000-0000-000000000001'),
(4,  NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'READ',   'View faculty',         'FACULTY',      gen_random_uuid(), 'faculty',       '11111111-0000-0000-0000-000000000001'),
(5,  NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'WRITE',  'Create/edit faculty',  'FACULTY',      gen_random_uuid(), 'faculty',       '11111111-0000-0000-0000-000000000001'),
(6,  NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'READ',   'View attendance',      'ATTENDANCE',   gen_random_uuid(), 'attendance',    '11111111-0000-0000-0000-000000000001'),
(7,  NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'WRITE',  'Mark attendance',      'ATTENDANCE',   gen_random_uuid(), 'attendance',    '11111111-0000-0000-0000-000000000001'),
(8,  NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'READ',   'View exams',           'EXAM',         gen_random_uuid(), 'exams',         '11111111-0000-0000-0000-000000000001'),
(9,  NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'WRITE',  'Manage exams',         'EXAM',         gen_random_uuid(), 'exams',         '11111111-0000-0000-0000-000000000001'),
(10, NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'READ',   'View fees',            'FEE',          gen_random_uuid(), 'fees',          '11111111-0000-0000-0000-000000000001'),
(11, NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'WRITE',  'Manage fees',          'FEE',          gen_random_uuid(), 'fees',          '11111111-0000-0000-0000-000000000001'),
(12, NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'READ',   'View timetable',       'TIMETABLE',    gen_random_uuid(), 'timetable',     '11111111-0000-0000-0000-000000000001'),
(13, NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'WRITE',  'Manage timetable',     'TIMETABLE',    gen_random_uuid(), 'timetable',     '11111111-0000-0000-0000-000000000001'),
(14, NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'READ',   'View announcements',   'ANNOUNCEMENT', gen_random_uuid(), 'announcements', '11111111-0000-0000-0000-000000000001'),
(15, NOW(), 1, NULL, NULL, FALSE, NOW(), 1, 'WRITE',  'Post announcements',   'ANNOUNCEMENT', gen_random_uuid(), 'announcements', '11111111-0000-0000-0000-000000000001');

SELECT setval('public.permissions_permission_id_seq', 15, true);

-- ---------------------------------------------------------------------------
-- 29. ROLE_PERMISSIONS  (WITH time zone)
--     ADMIN(2)   → all 15 permissions
--     FACULTY(4) → read students, mark/view attendance, view/manage exams,
--                  read timetable, read/post announcements
--     STUDENT(5) → read attendance, read exams, read fees,
--                  read timetable, read announcements
-- ---------------------------------------------------------------------------
INSERT INTO public.role_permissions (
    role_permission_id, created_at, created_by, deleted_at, deleted_by,
    is_deleted, updated_at, updated_by, permission_id, role_id,
    role_permission_public_id, tenant_id
) VALUES
-- ADMIN
( 1, NOW(),1,NULL,NULL,FALSE,NOW(),1,  1, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
( 2, NOW(),1,NULL,NULL,FALSE,NOW(),1,  2, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
( 3, NOW(),1,NULL,NULL,FALSE,NOW(),1,  3, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
( 4, NOW(),1,NULL,NULL,FALSE,NOW(),1,  4, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
( 5, NOW(),1,NULL,NULL,FALSE,NOW(),1,  5, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
( 6, NOW(),1,NULL,NULL,FALSE,NOW(),1,  6, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
( 7, NOW(),1,NULL,NULL,FALSE,NOW(),1,  7, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
( 8, NOW(),1,NULL,NULL,FALSE,NOW(),1,  8, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
( 9, NOW(),1,NULL,NULL,FALSE,NOW(),1,  9, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(10, NOW(),1,NULL,NULL,FALSE,NOW(),1, 10, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(11, NOW(),1,NULL,NULL,FALSE,NOW(),1, 11, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(12, NOW(),1,NULL,NULL,FALSE,NOW(),1, 12, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(13, NOW(),1,NULL,NULL,FALSE,NOW(),1, 13, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(14, NOW(),1,NULL,NULL,FALSE,NOW(),1, 14, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(15, NOW(),1,NULL,NULL,FALSE,NOW(),1, 15, 2, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
-- FACULTY
(16, NOW(),1,NULL,NULL,FALSE,NOW(),1,  1, 4, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(17, NOW(),1,NULL,NULL,FALSE,NOW(),1,  6, 4, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(18, NOW(),1,NULL,NULL,FALSE,NOW(),1,  7, 4, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(19, NOW(),1,NULL,NULL,FALSE,NOW(),1,  8, 4, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(20, NOW(),1,NULL,NULL,FALSE,NOW(),1,  9, 4, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(21, NOW(),1,NULL,NULL,FALSE,NOW(),1, 12, 4, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(22, NOW(),1,NULL,NULL,FALSE,NOW(),1, 14, 4, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(23, NOW(),1,NULL,NULL,FALSE,NOW(),1, 15, 4, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
-- STUDENT
(24, NOW(),1,NULL,NULL,FALSE,NOW(),1,  6, 5, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(25, NOW(),1,NULL,NULL,FALSE,NOW(),1,  8, 5, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(26, NOW(),1,NULL,NULL,FALSE,NOW(),1, 10, 5, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(27, NOW(),1,NULL,NULL,FALSE,NOW(),1, 12, 5, gen_random_uuid(),'11111111-0000-0000-0000-000000000001'),
(28, NOW(),1,NULL,NULL,FALSE,NOW(),1, 14, 5, gen_random_uuid(),'11111111-0000-0000-0000-000000000001');

SELECT setval('public.role_permissions_role_permission_id_seq', 28, true);

COMMIT;

-- =============================================================================
-- Verify row counts
-- =============================================================================
SELECT 'tenants'               AS tbl, COUNT(*) FROM public.tenants
UNION ALL SELECT 'users',               COUNT(*) FROM public.users
UNION ALL SELECT 'user_roles',          COUNT(*) FROM public.user_roles
UNION ALL SELECT 'roles',               COUNT(*) FROM public.roles
UNION ALL SELECT 'permissions',         COUNT(*) FROM public.permissions
UNION ALL SELECT 'role_permissions',    COUNT(*) FROM public.role_permissions
UNION ALL SELECT 'departments',         COUNT(*) FROM public.departments
UNION ALL SELECT 'academic_years',      COUNT(*) FROM public.academic_years
UNION ALL SELECT 'programs',            COUNT(*) FROM public.programs
UNION ALL SELECT 'courses',             COUNT(*) FROM public.courses
UNION ALL SELECT 'program_courses',     COUNT(*) FROM public.program_courses
UNION ALL SELECT 'faculty',             COUNT(*) FROM public.faculty
UNION ALL SELECT 'faculty_education',   COUNT(*) FROM public.faculty_education
UNION ALL SELECT 'faculty_work_exp',    COUNT(*) FROM public.faculty_work_experience
UNION ALL SELECT 'batches',             COUNT(*) FROM public.batches
UNION ALL SELECT 'students',            COUNT(*) FROM public.students
UNION ALL SELECT 'rooms',               COUNT(*) FROM public.rooms
UNION ALL SELECT 'course_assignments',  COUNT(*) FROM public.course_assignments
UNION ALL SELECT 'timetable_slots',     COUNT(*) FROM public.timetable_slots
UNION ALL SELECT 'attendance_sessions', COUNT(*) FROM public.attendance_sessions
UNION ALL SELECT 'attendance_records',  COUNT(*) FROM public.attendance_records
UNION ALL SELECT 'exams',               COUNT(*) FROM public.exams
UNION ALL SELECT 'exam_schedules',      COUNT(*) FROM public.exam_schedules
UNION ALL SELECT 'marks',               COUNT(*) FROM public.marks
UNION ALL SELECT 'fee_structures',      COUNT(*) FROM public.fee_structures
UNION ALL SELECT 'fee_invoices',        COUNT(*) FROM public.fee_invoices
UNION ALL SELECT 'fee_payments',        COUNT(*) FROM public.fee_payments
UNION ALL SELECT 'holidays',            COUNT(*) FROM public.holidays
UNION ALL SELECT 'announcements',       COUNT(*) FROM public.announcements
UNION ALL SELECT 'leave_applications',  COUNT(*) FROM public.leave_applications
ORDER BY tbl;
