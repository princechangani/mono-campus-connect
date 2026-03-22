-- =============================================================================
-- CampusConnect — Seed Data (Development / Testing)
-- Run AFTER 01_postgres_schema.sql
-- =============================================================================

BEGIN;

-- ---------------------------------------------------------------------------
-- Tenants
-- ---------------------------------------------------------------------------
INSERT INTO tenants (tenant_public_id, name, slug, domain, plan, timezone, locale,
                     primary_color, max_students, max_faculty, is_active,
                     created_at, is_deleted)
VALUES
  ('11111111-0000-0000-0000-000000000001', 'MIT College of Engineering', 'mit-coep',
   'campus.mitcoep.edu', 'enterprise', 'Asia/Kolkata', 'en', '#4F46E5', 10000, 1000, TRUE,
   NOW(), FALSE),
  ('11111111-0000-0000-0000-000000000002', 'Gujarat University', 'gu',
   'campus.gu.ac.in', 'pro', 'Asia/Kolkata', 'en', '#059669', 5000, 500, TRUE,
   NOW(), FALSE);

-- ---------------------------------------------------------------------------
-- Users  (password_hash = bcrypt of "password123" — never use in prod)
-- Note: Use proper bcrypt hashing in production (min 12 rounds)
-- ---------------------------------------------------------------------------
INSERT INTO users (user_public_id, tenant_id, email, password_hash,
                   first_name, last_name, phone, is_active, locale,
                   created_at, is_deleted)
VALUES
  -- MIT COEP Tenant (ID: 1)
  ('aaaaaaaa-0000-0000-0000-000000000001', 1, 'superadmin@mitcoep.edu',
   '$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUe', 'Vikram', 'Singh', '9000000001', TRUE, 'en',
   NOW(), FALSE),
  ('aaaaaaaa-0000-0000-0000-000000000002', 1, 'admin.cs@mitcoep.edu',
   '$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUe', 'Rajesh', 'Sharma', '9000000002', TRUE, 'en',
   NOW(), FALSE),
  ('aaaaaaaa-0000-0000-0000-000000000003', 1, 'prof.mehta@mitcoep.edu',
   '$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUe', 'Priya', 'Mehta', '9000000003', TRUE, 'en',
   NOW(), FALSE),
  ('aaaaaaaa-0000-0000-0000-000000000004', 1, 'prof.verma@mitcoep.edu',
   '$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUe', 'Amit', 'Verma', '9000000011', TRUE, 'en',
   NOW(), FALSE),
  -- Students (CS-2024 Batch)
  ('aaaaaaaa-0000-0000-0000-000000000005', 1, 'student.arjun@mitcoep.edu',
   '$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUe', 'Arjun', 'Patel', '9000000004', TRUE, 'en',
   NOW(), FALSE),
  ('aaaaaaaa-0000-0000-0000-000000000006', 1, 'student.neha@mitcoep.edu',
   '$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUe', 'Neha', 'Joshi', '9000000005', TRUE, 'en',
   NOW(), FALSE),
  ('aaaaaaaa-0000-0000-0000-000000000007', 1, 'student.karan@mitcoep.edu',
   '$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUe', 'Karan', 'Singh', '9000000006', TRUE, 'en',
   NOW(), FALSE),
  ('aaaaaaaa-0000-0000-0000-000000000008', 1, 'student.priya@mitcoep.edu',
   '$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUe', 'Priya', 'Kapoor', '9000000007', TRUE, 'en',
   NOW(), FALSE),
  -- Gujarat University Tenant (ID: 2)
  ('aaaaaaaa-0000-0000-0000-000000000009', 2, 'admin@gu.ac.in',
   '$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUe', 'Ashok', 'Kumar', '9100000001', TRUE, 'en',
   NOW(), FALSE);

-- ---------------------------------------------------------------------------
-- Roles
-- ---------------------------------------------------------------------------
INSERT INTO roles (role_public_id, tenant_id, name, description, is_system,
                   created_at, is_deleted)
VALUES
  ('bbbbbbbb-0000-0000-0000-000000000001', 1, 'super_admin', 'Full system access', TRUE, NOW(), FALSE),
  ('bbbbbbbb-0000-0000-0000-000000000002', 1, 'admin',       'College admin',       TRUE, NOW(), FALSE),
  ('bbbbbbbb-0000-0000-0000-000000000003', 1, 'faculty',     'Teaching staff',      TRUE, NOW(), FALSE),
  ('bbbbbbbb-0000-0000-0000-000000000004', 1, 'student',     'Enrolled student',    TRUE, NOW(), FALSE),
  ('bbbbbbbb-0000-0000-0000-000000000005', 1, 'hod',         'Head of department',  TRUE, NOW(), FALSE);

-- ---------------------------------------------------------------------------
-- Permissions
-- ---------------------------------------------------------------------------
INSERT INTO permissions (permission_public_id, tenant_id, module, action, resource, description, created_at, is_deleted)
VALUES
  ('cc000000-0000-0000-0000-000000000001', 1, 'attendance', 'create', 'session', 'Create attendance session', NOW(), FALSE),
  ('cc000000-0000-0000-0000-000000000002', 1, 'attendance', 'read',   'session', 'View attendance session', NOW(), FALSE),
  ('cc000000-0000-0000-0000-000000000003', 1, 'marks',      'create', 'record',  'Add marks', NOW(), FALSE),
  ('cc000000-0000-0000-0000-000000000004', 1, 'marks',      'read',   'record',  'View marks', NOW(), FALSE);

-- ---------------------------------------------------------------------------
-- Role Permissions
-- ---------------------------------------------------------------------------
INSERT INTO role_permissions (role_permission_public_id, tenant_id, role_id, permission_id, created_at, is_deleted)
VALUES
  ('cd000000-0000-0000-0000-000000000001', 1, 3, 1, NOW(), FALSE), -- Faculty can create attendance
  ('cd000000-0000-0000-0000-000000000002', 1, 3, 2, NOW(), FALSE), -- Faculty can read attendance
  ('cd000000-0000-0000-0000-000000000003', 1, 4, 2, NOW(), FALSE), -- Student can read attendance
  ('cd000000-0000-0000-0000-000000000004', 1, 3, 3, NOW(), FALSE), -- Faculty can add marks
  ('cd000000-0000-0000-0000-000000000005', 1, 3, 4, NOW(), FALSE), -- Faculty can read marks
  ('cd000000-0000-0000-0000-000000000006', 1, 4, 4, NOW(), FALSE); -- Student can read marks

-- ---------------------------------------------------------------------------
-- User → Role assignments
-- ---------------------------------------------------------------------------
INSERT INTO user_roles (user_role_public_id, tenant_id, user_id, role_id, created_at, is_deleted)
VALUES
  ('cccccccc-0000-0000-0000-000000000001', 1, 1, 1, NOW(), FALSE),
  ('cccccccc-0000-0000-0000-000000000002', 1, 2, 5, NOW(), FALSE),
  ('cccccccc-0000-0000-0000-000000000003', 1, 2, 3, NOW(), FALSE),
  ('cccccccc-0000-0000-0000-000000000004', 1, 3, 3, NOW(), FALSE),
  ('cccccccc-0000-0000-0000-000000000005', 1, 4, 4, NOW(), FALSE),
  ('cccccccc-0000-0000-0000-000000000006', 1, 5, 4, NOW(), FALSE);

-- ---------------------------------------------------------------------------
-- Departments
-- ---------------------------------------------------------------------------
INSERT INTO departments (department_public_id, tenant_id, name, code, description, created_at, is_deleted)
VALUES
  ('dddddddd-0000-0000-0000-000000000001', 1, 'Computer Science & Engineering', 'CS',  'CS dept', NOW(), FALSE),
  ('dddddddd-0000-0000-0000-000000000002', 1, 'Mechanical Engineering',         'ME',  'ME dept', NOW(), FALSE),
  ('dddddddd-0000-0000-0000-000000000003', 1, 'Electronics & Communication',    'EC',  'EC dept', NOW(), FALSE);

-- ---------------------------------------------------------------------------
-- Academic years
-- ---------------------------------------------------------------------------
INSERT INTO academic_years (academic_year_public_id, tenant_id, label, start_date, end_date, is_current, created_at, is_deleted)
VALUES
  ('eeeeeeee-0000-0000-0000-000000000001', 1, '2023-24', '2023-07-01', '2024-06-30', FALSE, NOW(), FALSE),
  ('eeeeeeee-0000-0000-0000-000000000002', 1, '2024-25', '2024-07-01', '2025-06-30', TRUE, NOW(), FALSE);

-- ---------------------------------------------------------------------------
-- Programs
-- ---------------------------------------------------------------------------
INSERT INTO programs (program_public_id, tenant_id, department_id, name, code,
                      degree_level, duration_years, total_semesters, total_credits, created_at, is_deleted)
VALUES
  ('ffffffff-0000-0000-0000-000000000001', 1, 1, 'B.Tech Computer Science',  'BTCS', 'UG', 4, 8, 160, NOW(), FALSE),
  ('ffffffff-0000-0000-0000-000000000002', 1, 1, 'M.Tech Computer Science',  'MTCS', 'PG', 2, 4,  80, NOW(), FALSE),
  ('ffffffff-0000-0000-0000-000000000003', 1, 2, 'B.Tech Mechanical',        'BTME', 'UG', 4, 8, 160, NOW(), FALSE);

-- ---------------------------------------------------------------------------
-- Courses
-- ---------------------------------------------------------------------------
INSERT INTO courses (course_public_id, tenant_id, department_id, name, code,
                     credits, lecture_hours_per_week, lab_hours_per_week, course_type)
VALUES
  ('10000000-0000-0000-0000-000000000001', 1, 1, 'Data Structures & Algorithms', 'CS201', 4, 3, 2, 'theory'),
  ('10000000-0000-0000-0000-000000000002', 1, 1, 'Database Management Systems',  'CS301', 4, 3, 2, 'theory'),
  ('10000000-0000-0000-0000-000000000003', 1, 1, 'Operating Systems',            'CS302', 3, 3, 0, 'theory'),
  ('10000000-0000-0000-0000-000000000004', 1, 1, 'Computer Networks',            'CS401', 4, 3, 2, 'theory'),
  ('10000000-0000-0000-0000-000000000005', 1, 1, 'Advanced Algorithms (Elective)','CS501',3, 3, 0, 'elective');

-- ---------------------------------------------------------------------------
-- Program curriculum (program_courses)
-- ---------------------------------------------------------------------------
INSERT INTO program_courses (program_course_public_id, tenant_id, program_id, course_id,
                              semester_number, is_elective, is_mandatory)
VALUES
  ('20000000-0000-0000-0000-000000000001', 1, 1, 1, 2, FALSE, TRUE),  -- DSA in sem 2
  ('20000000-0000-0000-0000-000000000002', 1, 1, 2, 3, FALSE, TRUE),  -- DBMS in sem 3
  ('20000000-0000-0000-0000-000000000003', 1, 1, 3, 3, FALSE, TRUE),  -- OS in sem 3
  ('20000000-0000-0000-0000-000000000004', 1, 1, 4, 4, FALSE, TRUE),  -- Networks in sem 4
  ('20000000-0000-0000-0000-000000000005', 1, 1, 5, 5, TRUE,  FALSE); -- Elective in sem 5

-- ---------------------------------------------------------------------------
-- Batches
-- ---------------------------------------------------------------------------
INSERT INTO batches (batch_public_id, tenant_id, program_id, academic_year_id,
                     name, current_semester, max_students)
VALUES
  ('30000000-0000-0000-0000-000000000001', 1, 1, 2, 'CS-2024', 1, 60),
  ('30000000-0000-0000-0000-000000000002', 1, 1, 1, 'CS-2023', 3, 60),
  ('30000000-0000-0000-0000-000000000003', 1, 3, 2, 'ME-2024', 1, 60);

-- ---------------------------------------------------------------------------
-- Faculty
-- ---------------------------------------------------------------------------
INSERT INTO faculty (faculty_public_id, tenant_id, user_id, department_id,
                     employee_id, designation, employment_type, joining_date,
                     highest_qualification, specialization, is_hod)
VALUES
  ('40000000-0000-0000-0000-000000000001', 1, 2, 1, 'EMP001', 'Professor',
   'permanent', '2010-07-15', 'Ph.D', 'Algorithms & AI', TRUE),
  ('40000000-0000-0000-0000-000000000002', 1, 3, 1, 'EMP002', 'Assistant Professor',
   'permanent', '2018-07-01', 'M.Tech', 'Databases & Cloud', FALSE);

-- Set HOD for CS department
UPDATE departments SET head_faculty_id = 1 WHERE department_id = 1;
-- Set class teachers
UPDATE batches SET class_teacher_id = 2 WHERE batch_id IN (1, 2);

-- ---------------------------------------------------------------------------
-- Faculty education & work experience
-- ---------------------------------------------------------------------------
INSERT INTO faculty_education (faculty_education_public_id, tenant_id, faculty_id,
                                degree, field_of_study, institution, passing_year)
VALUES
  ('50000000-0000-0000-0000-000000000001', 1, 1, 'Ph.D',   'Computer Science', 'IIT Bombay', 2008),
  ('50000000-0000-0000-0000-000000000002', 1, 1, 'M.Tech', 'Computer Science', 'IIT Bombay', 2003),
  ('50000000-0000-0000-0000-000000000003', 1, 2, 'M.Tech', 'Information Technology', 'VJTI Mumbai', 2016);

INSERT INTO faculty_work_experience (work_experience_public_id, tenant_id, faculty_id,
                                     organization, role, from_date, to_date)
VALUES
  ('60000000-0000-0000-0000-000000000001', 1, 2, 'TCS Innovations', 'Software Engineer',
   '2016-08-01', '2018-06-30');

-- ---------------------------------------------------------------------------
-- Students
-- ---------------------------------------------------------------------------
INSERT INTO students (student_public_id, tenant_id, user_id, batch_id,
                      enrollment_number, enrollment_date, current_semester,
                      admission_type, category, blood_group,
                      guardian_name, guardian_phone, guardian_relation,
                      is_hosteller, status)
VALUES
  ('70000000-0000-0000-0000-000000000001', 1, 4, 1, '24CS001', '2024-07-10', 1,
   'regular', 'general', 'O+', 'Mahesh Patel', '9111111111', 'Father', FALSE, 'active'),
  ('70000000-0000-0000-0000-000000000002', 1, 5, 1, '24CS002', '2024-07-10', 1,
   'regular', 'OBC', 'B+', 'Suresh Joshi', '9222222222', 'Father', TRUE, 'active');

-- ---------------------------------------------------------------------------
-- Course assignments
-- ---------------------------------------------------------------------------
INSERT INTO course_assignments (course_assignment_public_id, tenant_id, course_id,
                                 faculty_id, batch_id, academic_year_id,
                                 semester_number, section, is_primary)
VALUES
  ('80000000-0000-0000-0000-000000000001', 1, 2, 1, 2, 1, 3, 'A', TRUE),  -- Rajesh → DBMS → CS-2023
  ('80000000-0000-0000-0000-000000000002', 1, 3, 2, 2, 1, 3, 'A', TRUE);  -- Priya  → OS   → CS-2023

-- ---------------------------------------------------------------------------
-- Rooms
-- ---------------------------------------------------------------------------
INSERT INTO rooms (room_public_id, tenant_id, building, floor, room_number,
                   name, capacity, room_type, has_projector, has_ac)
VALUES
  ('90000000-0000-0000-0000-000000000001', 1, 'A Block', 1, 'A101', 'Classroom A101', 60, 'classroom', TRUE,  TRUE),
  ('90000000-0000-0000-0000-000000000002', 1, 'A Block', 1, 'A102', 'Classroom A102', 60, 'classroom', TRUE,  TRUE),
  ('90000000-0000-0000-0000-000000000003', 1, 'B Block', 0, 'B001', 'CS Lab 1',       30, 'lab',       TRUE,  TRUE),
  ('90000000-0000-0000-0000-000000000004', 1, 'C Block', 2, 'C201', 'Seminar Hall',  150, 'seminar',   TRUE,  TRUE);

-- ---------------------------------------------------------------------------
-- Timetable slots
-- ---------------------------------------------------------------------------
INSERT INTO timetable_slots (slot_public_id, tenant_id, course_assignment_id, room_id,
                              day_of_week, start_time, end_time, slot_type, effective_from)
VALUES
  ('a0000000-0000-0000-0000-000000000001', 1, 1, 1, 1, '09:00', '10:00', 'lecture', '2024-07-15'),
  ('a0000000-0000-0000-0000-000000000002', 1, 1, 1, 3, '09:00', '10:00', 'lecture', '2024-07-15'),
  ('a0000000-0000-0000-0000-000000000003', 1, 2, 2, 2, '10:00', '11:00', 'lecture', '2024-07-15'),
  ('a0000000-0000-0000-0000-000000000004', 1, 2, 2, 4, '10:00', '11:00', 'lecture', '2024-07-15');

-- ---------------------------------------------------------------------------
-- Attendance session + records (sample)
-- ---------------------------------------------------------------------------
INSERT INTO attendance_sessions (session_public_id, tenant_id, course_assignment_id,
                                  slot_id, room_id, session_date, start_time, end_time,
                                  topic_covered, session_type, conducted_by)
VALUES
  ('b0000000-0000-0000-0000-000000000001', 1, 1, 1, 1,
   '2024-08-05', '09:00', '10:00',
   'Introduction to Relational Model', 'lecture', 1);

INSERT INTO attendance_records (attendance_record_public_id, tenant_id, session_id,
                                 student_id, status, marked_by)
VALUES
  ('c0000000-0000-0000-0000-000000000001', 1, 1, 1, 'present', 1),
  ('c0000000-0000-0000-0000-000000000002', 1, 1, 2, 'absent',  1);

-- ---------------------------------------------------------------------------
-- Exam + marks (sample)
-- ---------------------------------------------------------------------------
INSERT INTO exams (exam_public_id, tenant_id, academic_year_id, batch_id,
                   name, exam_type, semester_number, start_date, end_date,
                   total_marks, passing_marks)
VALUES
  ('d0000000-0000-0000-0000-000000000001', 1, 1, 2,
   'Mid-Semester I', 'internal', 3, '2023-09-20', '2023-09-25', 30, 12);

INSERT INTO exam_schedules (exam_schedule_public_id, tenant_id, exam_id, course_id,
                             room_id, exam_date, start_time, end_time, invigilator_id)
VALUES
  ('e0000000-0000-0000-0000-000000000001', 1, 1, 2, 1, '2023-09-21', '10:00', '12:00', 2);

INSERT INTO marks (mark_public_id, tenant_id, exam_id, student_id, course_id,
                   marks_obtained, total_marks, grade, grade_points, entered_by)
VALUES
  ('f0000000-0000-0000-0000-000000000001', 1, 1, 1, 2, 24.5, 30, 'A', 9.0, 1),
  ('f0000000-0000-0000-0000-000000000002', 1, 1, 2, 2, 19.0, 30, 'B', 7.0, 1);

-- ---------------------------------------------------------------------------
-- Fee structure + invoice + payment (sample)
-- ---------------------------------------------------------------------------
INSERT INTO fee_structures (fee_structure_public_id, tenant_id, program_id, academic_year_id,
                             semester_number, category, fee_name, total_amount,
                             due_date, late_fee_per_day)
VALUES
  ('fe000000-0000-0000-0000-000000000001', 1, 1, 2, 1, NULL,
   'Tuition Fee — Sem 1', 45000.00, '2024-08-31', 50.00),
  ('fe000000-0000-0000-0000-000000000002', 1, 1, 2, 1, NULL,
   'Development Fee — Sem 1', 5000.00, '2024-08-31', 0.00);

INSERT INTO fee_invoices (invoice_public_id, tenant_id, student_id, fee_structure_id,
                          invoice_number, issue_date, due_date,
                          subtotal, discount, late_fee, total_amount, status)
VALUES
  ('in000000-0000-0000-0000-000000000001', 1, 1, 1,
   'INV-2024-00001', '2024-07-15', '2024-08-31',
   45000.00, 0, 0, 45000.00, 'sent');

INSERT INTO fee_payments (payment_public_id, tenant_id, invoice_id, student_id,
                          amount_paid, payment_date, payment_mode,
                          transaction_reference, receipt_number, status)
VALUES
  ('py000000-0000-0000-0000-000000000001', 1, 1, 1,
   45000.00, '2024-08-28', 'upi',
   'UPI123456789', 'REC-2024-00001', 'success');

-- ---------------------------------------------------------------------------
-- Holidays & Announcements
-- ---------------------------------------------------------------------------
INSERT INTO holidays (holiday_public_id, tenant_id, title, holiday_date,
                      holiday_type, applies_to)
VALUES
  ('ho000000-0000-0000-0000-000000000001', 1, 'Republic Day',    '2025-01-26', 'national', 'all'),
  ('ho000000-0000-0000-0000-000000000002', 1, 'Independence Day','2025-08-15', 'national', 'all'),
  ('ho000000-0000-0000-0000-000000000003', 1, 'Diwali',          '2025-10-20', 'state',    'all'),
  ('ho000000-0000-0000-0000-000000000004', 1, 'College Foundation Day', '2025-09-05', 'college', 'all');

INSERT INTO announcements (announcement_public_id, tenant_id, title, content,
                           audience, priority, published_at)
VALUES
  ('an000000-0000-0000-0000-000000000001', 1,
   'Mid-semester examination schedule released',
   'The mid-semester examination schedule for Semester 3 has been published. Please check the timetable section.',
   'students', 'high', NOW()),
  ('an000000-0000-0000-0000-000000000002', 1,
   'Faculty development programme — 10 Sept 2024',
   'All faculty are requested to attend the FDP on 10 Sept 2024 in Seminar Hall C201.',
   'faculty', 'normal', NOW());

-- ---------------------------------------------------------------------------
-- Leave Applications
-- ---------------------------------------------------------------------------
INSERT INTO leave_applications (leave_public_id, tenant_id, applicant_user_id, applicant_type, leave_type, from_date, to_date, reason, status, created_at, is_deleted)
VALUES
  ('la000000-0000-0000-0000-000000000001', 1, 4, 'student', 'medical', '2024-09-10', '2024-09-12', 'Viral fever', 'approved', NOW(), FALSE),
  ('la000000-0000-0000-0000-000000000002', 1, 3, 'faculty', 'personal', '2024-10-05', '2024-10-06', 'Family function', 'pending', NOW(), FALSE);

COMMIT;

-- Verify counts
SELECT 'tenants'              AS tbl, COUNT(*) FROM tenants
UNION ALL SELECT 'users',              COUNT(*) FROM users
UNION ALL SELECT 'departments',        COUNT(*) FROM departments
UNION ALL SELECT 'roles',              COUNT(*) FROM roles
UNION ALL SELECT 'permissions',        COUNT(*) FROM permissions
UNION ALL SELECT 'role_permissions',   COUNT(*) FROM role_permissions
UNION ALL SELECT 'programs',           COUNT(*) FROM programs
UNION ALL SELECT 'courses',            COUNT(*) FROM courses
UNION ALL SELECT 'batches',            COUNT(*) FROM batches
UNION ALL SELECT 'faculty',            COUNT(*) FROM faculty
UNION ALL SELECT 'students',           COUNT(*) FROM students
UNION ALL SELECT 'course_assignments', COUNT(*) FROM course_assignments
UNION ALL SELECT 'rooms',              COUNT(*) FROM rooms
UNION ALL SELECT 'timetable_slots',    COUNT(*) FROM timetable_slots
UNION ALL SELECT 'attendance_sessions',COUNT(*) FROM attendance_sessions
UNION ALL SELECT 'attendance_records', COUNT(*) FROM attendance_records
UNION ALL SELECT 'exams',              COUNT(*) FROM exams
UNION ALL SELECT 'marks',              COUNT(*) FROM marks
UNION ALL SELECT 'fee_structures',     COUNT(*) FROM fee_structures
UNION ALL SELECT 'fee_invoices',       COUNT(*) FROM fee_invoices
UNION ALL SELECT 'fee_payments',       COUNT(*) FROM fee_payments
UNION ALL SELECT 'holidays',           COUNT(*) FROM holidays
UNION ALL SELECT 'announcements',      COUNT(*) FROM announcements
UNION ALL SELECT 'leave_applications', COUNT(*) FROM leave_applications
ORDER BY tbl;
