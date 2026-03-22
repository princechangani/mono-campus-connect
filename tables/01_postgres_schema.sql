-- =============================================================================
-- CampusConnect — PostgreSQL Schema
-- Multitenant SaaS College Management System
-- =============================================================================
-- Convention:
--   • Every table has: {table}_id BIGINT PK, {table}_public_id UUID UNIQUE,
--     tenant_id BIGINT FK, created_at, created_by, updated_at, updated_by,
--     deleted_at, deleted_by, is_deleted (soft-delete pattern)
--   • Multitenancy: shared schema, tenant_id on every row
--   • public_id (UUID) is the only ID exposed in APIs / URLs
-- =============================================================================

-- ---------------------------------------------------------------------------
-- Extensions
-- ---------------------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "citext";   -- case-insensitive text for email


-- =============================================================================
-- GROUP 1 — TENANT & IDENTITY
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 1.1  tenants
-- ---------------------------------------------------------------------------
CREATE TABLE tenants (
    tenant_id                BIGSERIAL       PRIMARY KEY,
    tenant_public_id         UUID            NOT NULL DEFAULT uuid_generate_v4(),
    name                     VARCHAR(200)    NOT NULL,
    slug                     VARCHAR(100)    NOT NULL,                -- URL prefix  e.g.  mit
    domain                   VARCHAR(255),                           -- custom domain e.g. campus.edu
    plan                     VARCHAR(50)     NOT NULL DEFAULT 'basic',-- basic | pro | enterprise
    timezone                 VARCHAR(100)    NOT NULL DEFAULT 'UTC',
    locale                   VARCHAR(20)     NOT NULL DEFAULT 'en',
    logo_url                 TEXT,
    primary_color            VARCHAR(7),                             -- hex e.g. #4F46E5
    max_students             INT             NOT NULL DEFAULT 5000,
    max_faculty              INT             NOT NULL DEFAULT 500,
    is_active                BOOLEAN         NOT NULL DEFAULT TRUE,
    subscription_expires_at  TIMESTAMPTZ,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT,
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT,
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT,
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_tenants_public_id  UNIQUE (tenant_public_id),
    CONSTRAINT uq_tenants_slug       UNIQUE (slug),
    CONSTRAINT uq_tenants_domain     UNIQUE (domain)
);

-- ---------------------------------------------------------------------------
-- 1.2  users  (universal identity — all roles share this table)
-- ---------------------------------------------------------------------------
CREATE TABLE users (
    user_id                  BIGSERIAL       PRIMARY KEY,
    user_public_id           UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    email                    CITEXT          NOT NULL,
    email_verified_at        TIMESTAMPTZ,
    password_hash            TEXT            NOT NULL,
    first_name               VARCHAR(100)    NOT NULL,
    last_name                VARCHAR(100)    NOT NULL,
    phone                    VARCHAR(20),
    phone_verified_at        TIMESTAMPTZ,
    avatar_url               TEXT,
    date_of_birth            DATE,
    gender                   VARCHAR(20),
    address_line1            TEXT,
    address_line2            TEXT,
    city                     VARCHAR(100),
    state                    VARCHAR(100),
    country                  VARCHAR(100),
    postal_code              VARCHAR(20),
    locale                   VARCHAR(20)     NOT NULL DEFAULT 'en',
    timezone                 VARCHAR(100),                           -- falls back to tenant timezone
    last_login_at            TIMESTAMPTZ,
    last_login_ip            INET,
    is_active                BOOLEAN         NOT NULL DEFAULT TRUE,
    mfa_enabled              BOOLEAN         NOT NULL DEFAULT FALSE,
    mfa_secret               TEXT,                                   -- stored encrypted
    password_reset_token     TEXT,
    password_reset_expires_at TIMESTAMPTZ,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_users_public_id UNIQUE (user_public_id)
);

-- Unique email per tenant (excluding soft-deleted rows)
CREATE UNIQUE INDEX uq_users_tenant_email
    ON users (tenant_id, email)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_users_tenant        ON users (tenant_id, is_deleted);
CREATE INDEX idx_users_last_login    ON users (tenant_id, last_login_at DESC);

-- ---------------------------------------------------------------------------
-- 1.3  departments
-- ---------------------------------------------------------------------------
CREATE TABLE departments (
    department_id            BIGSERIAL       PRIMARY KEY,
    department_public_id     UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    name                     VARCHAR(200)    NOT NULL,
    code                     VARCHAR(20)     NOT NULL,               -- e.g. CS, ME, MBA
    head_faculty_id          BIGINT,                                 -- FK set after faculty table
    parent_department_id     BIGINT          REFERENCES departments(department_id),
    description              TEXT,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_departments_public_id UNIQUE (department_public_id)
);

CREATE UNIQUE INDEX uq_departments_tenant_code
    ON departments (tenant_id, code)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_departments_tenant ON departments (tenant_id, is_deleted);

-- ---------------------------------------------------------------------------
-- 1.4  roles
-- ---------------------------------------------------------------------------
CREATE TABLE roles (
    role_id                  BIGSERIAL       PRIMARY KEY,
    role_public_id           UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    name                     VARCHAR(80)     NOT NULL,               -- super_admin | admin | faculty | student
    description              TEXT,
    is_system                BOOLEAN         NOT NULL DEFAULT FALSE, -- system roles cannot be deleted
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_roles_public_id UNIQUE (role_public_id)
);

CREATE UNIQUE INDEX uq_roles_tenant_name
    ON roles (tenant_id, name)
    WHERE is_deleted = FALSE;

-- ---------------------------------------------------------------------------
-- 1.5  permissions
-- ---------------------------------------------------------------------------
CREATE TABLE permissions (
    permission_id            BIGSERIAL       PRIMARY KEY,
    permission_public_id     UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    module                   VARCHAR(80)     NOT NULL,               -- attendance | marks | finance …
    action                   VARCHAR(80)     NOT NULL,               -- create | read | update | delete | export
    resource                 VARCHAR(80)     NOT NULL,               -- session | record | invoice …
    description              TEXT,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_permissions_public_id UNIQUE (permission_public_id)
);

CREATE UNIQUE INDEX uq_permissions_tenant_module_action_resource
    ON permissions (tenant_id, module, action, resource)
    WHERE is_deleted = FALSE;

-- ---------------------------------------------------------------------------
-- 1.6  role_permissions  (junction)
-- ---------------------------------------------------------------------------
CREATE TABLE role_permissions (
    role_permission_id       BIGSERIAL       PRIMARY KEY,
    role_permission_public_id UUID           NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    role_id                  BIGINT          NOT NULL REFERENCES roles(role_id),
    permission_id            BIGINT          NOT NULL REFERENCES permissions(permission_id),
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_role_permissions_public_id UNIQUE (role_permission_public_id)
);

CREATE UNIQUE INDEX uq_role_permissions_role_perm
    ON role_permissions (tenant_id, role_id, permission_id)
    WHERE is_deleted = FALSE;

-- ---------------------------------------------------------------------------
-- 1.7  user_roles  (junction — supports scoped roles e.g. HOD of dept X)
-- ---------------------------------------------------------------------------
CREATE TABLE user_roles (
    user_role_id             BIGSERIAL       PRIMARY KEY,
    user_role_public_id      UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    user_id                  BIGINT          NOT NULL REFERENCES users(user_id),
    role_id                  BIGINT          NOT NULL REFERENCES roles(role_id),
    context_type             VARCHAR(50),                            -- 'department' | 'batch' | NULL = global
    context_id               BIGINT,                                 -- FK value (polymorphic)
    assigned_at              TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    expires_at               TIMESTAMPTZ,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_user_roles_public_id UNIQUE (user_role_public_id)
);

CREATE UNIQUE INDEX uq_user_roles_user_role_context
    ON user_roles (tenant_id, user_id, role_id, COALESCE(context_type,''), COALESCE(context_id, 0))
    WHERE is_deleted = FALSE;

CREATE INDEX idx_user_roles_user   ON user_roles (tenant_id, user_id, is_deleted);


-- =============================================================================
-- GROUP 2 — ACADEMIC DOMAIN
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 2.1  academic_years
-- ---------------------------------------------------------------------------
CREATE TABLE academic_years (
    academic_year_id         BIGSERIAL       PRIMARY KEY,
    academic_year_public_id  UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    label                    VARCHAR(20)     NOT NULL,               -- e.g. 2024-25
    start_date               DATE            NOT NULL,
    end_date                 DATE            NOT NULL,
    is_current               BOOLEAN         NOT NULL DEFAULT FALSE,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_academic_years_public_id UNIQUE (academic_year_public_id),
    CONSTRAINT chk_academic_year_dates     CHECK (end_date > start_date)
);

CREATE UNIQUE INDEX uq_academic_years_tenant_label
    ON academic_years (tenant_id, label)
    WHERE is_deleted = FALSE;

-- Only one current year per tenant
CREATE UNIQUE INDEX uq_academic_years_one_current
    ON academic_years (tenant_id)
    WHERE is_current = TRUE AND is_deleted = FALSE;

-- ---------------------------------------------------------------------------
-- 2.2  programs
-- ---------------------------------------------------------------------------
CREATE TABLE programs (
    program_id               BIGSERIAL       PRIMARY KEY,
    program_public_id        UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    department_id            BIGINT          NOT NULL REFERENCES departments(department_id),
    name                     VARCHAR(200)    NOT NULL,               -- B.Tech Computer Science
    code                     VARCHAR(30)     NOT NULL,               -- BTCS
    degree_level             VARCHAR(50)     NOT NULL,               -- UG | PG | PhD | Diploma
    duration_years           SMALLINT        NOT NULL,
    total_semesters          SMALLINT        NOT NULL,
    total_credits            SMALLINT        NOT NULL,
    is_active                BOOLEAN         NOT NULL DEFAULT TRUE,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_programs_public_id UNIQUE (program_public_id)
);

CREATE UNIQUE INDEX uq_programs_tenant_code
    ON programs (tenant_id, code)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_programs_department ON programs (tenant_id, department_id, is_deleted);

-- ---------------------------------------------------------------------------
-- 2.3  courses  (subject catalog)
-- ---------------------------------------------------------------------------
CREATE TABLE courses (
    course_id                BIGSERIAL       PRIMARY KEY,
    course_public_id         UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    department_id            BIGINT          NOT NULL REFERENCES departments(department_id),
    name                     VARCHAR(200)    NOT NULL,
    code                     VARCHAR(30)     NOT NULL,
    description              TEXT,
    credits                  SMALLINT        NOT NULL,
    lecture_hours_per_week   SMALLINT        NOT NULL DEFAULT 3,
    lab_hours_per_week       SMALLINT        NOT NULL DEFAULT 0,
    tutorial_hours_per_week  SMALLINT        NOT NULL DEFAULT 0,
    course_type              VARCHAR(50)     NOT NULL DEFAULT 'theory',-- theory | lab | elective | project
    is_active                BOOLEAN         NOT NULL DEFAULT TRUE,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_courses_public_id UNIQUE (course_public_id)
);

CREATE UNIQUE INDEX uq_courses_tenant_code
    ON courses (tenant_id, code)
    WHERE is_deleted = FALSE;

-- ---------------------------------------------------------------------------
-- 2.4  program_courses  (curriculum — which course in which semester)
-- ---------------------------------------------------------------------------
CREATE TABLE program_courses (
    program_course_id        BIGSERIAL       PRIMARY KEY,
    program_course_public_id UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    program_id               BIGINT          NOT NULL REFERENCES programs(program_id),
    course_id                BIGINT          NOT NULL REFERENCES courses(course_id),
    semester_number          SMALLINT        NOT NULL,
    is_elective              BOOLEAN         NOT NULL DEFAULT FALSE,
    is_mandatory             BOOLEAN         NOT NULL DEFAULT TRUE,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_program_courses_public_id UNIQUE (program_course_public_id)
);

CREATE UNIQUE INDEX uq_program_courses_program_course_sem
    ON program_courses (tenant_id, program_id, course_id, semester_number)
    WHERE is_deleted = FALSE;

-- ---------------------------------------------------------------------------
-- 2.5  batches  (a cohort of students: program + academic year)
-- ---------------------------------------------------------------------------
CREATE TABLE batches (
    batch_id                 BIGSERIAL       PRIMARY KEY,
    batch_public_id          UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    program_id               BIGINT          NOT NULL REFERENCES programs(program_id),
    academic_year_id         BIGINT          NOT NULL REFERENCES academic_years(academic_year_id),
    name                     VARCHAR(100)    NOT NULL,               -- e.g. CS-2022
    current_semester         SMALLINT        NOT NULL DEFAULT 1,
    max_students             INT             NOT NULL,
    class_teacher_id         BIGINT,                                 -- FK set after faculty table
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_batches_public_id UNIQUE (batch_public_id)
);

CREATE UNIQUE INDEX uq_batches_tenant_name
    ON batches (tenant_id, name)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_batches_program ON batches (tenant_id, program_id, is_deleted);

-- ---------------------------------------------------------------------------
-- 2.6  students  (extends users)
-- ---------------------------------------------------------------------------
CREATE TABLE students (
    student_id               BIGSERIAL       PRIMARY KEY,
    student_public_id        UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    user_id                  BIGINT          NOT NULL REFERENCES users(user_id),
    batch_id                 BIGINT          NOT NULL REFERENCES batches(batch_id),
    enrollment_number        VARCHAR(50)     NOT NULL,
    enrollment_date          DATE            NOT NULL,
    current_semester         SMALLINT        NOT NULL DEFAULT 1,
    admission_type           VARCHAR(50)     NOT NULL DEFAULT 'regular',-- regular | lateral | management
    category                 VARCHAR(50),                            -- general | SC | ST | OBC | EWS
    blood_group              VARCHAR(10),
    guardian_name            VARCHAR(200),
    guardian_phone           VARCHAR(20),
    guardian_relation        VARCHAR(50),
    previous_school          TEXT,
    is_hosteller             BOOLEAN         NOT NULL DEFAULT FALSE,
    status                   VARCHAR(30)     NOT NULL DEFAULT 'active',-- active | dropped | graduated | suspended
    graduated_at             DATE,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_students_public_id UNIQUE (student_public_id),
    CONSTRAINT uq_students_user      UNIQUE (user_id)
);

CREATE UNIQUE INDEX uq_students_tenant_enrollment
    ON students (tenant_id, enrollment_number)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_students_batch    ON students (tenant_id, batch_id, is_deleted);
CREATE INDEX idx_students_status   ON students (tenant_id, status, is_deleted);

-- ---------------------------------------------------------------------------
-- 2.7  faculty  (extends users)
-- ---------------------------------------------------------------------------
CREATE TABLE faculty (
    faculty_id               BIGSERIAL       PRIMARY KEY,
    faculty_public_id        UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    user_id                  BIGINT          NOT NULL REFERENCES users(user_id),
    department_id            BIGINT          NOT NULL REFERENCES departments(department_id),
    employee_id              VARCHAR(50)     NOT NULL,
    designation              VARCHAR(100)    NOT NULL,               -- Professor | Asst. Prof | Lecturer
    employment_type          VARCHAR(30)     NOT NULL DEFAULT 'permanent',-- permanent | contract | visiting
    joining_date             DATE            NOT NULL,
    leaving_date             DATE,
    highest_qualification    VARCHAR(100),
    specialization           TEXT,
    experience_years         SMALLINT,
    is_hod                   BOOLEAN         NOT NULL DEFAULT FALSE,
    status                   VARCHAR(30)     NOT NULL DEFAULT 'active',-- active | inactive | resigned
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_faculty_public_id UNIQUE (faculty_public_id),
    CONSTRAINT uq_faculty_user      UNIQUE (user_id)
);

CREATE UNIQUE INDEX uq_faculty_tenant_employee_id
    ON faculty (tenant_id, employee_id)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_faculty_department ON faculty (tenant_id, department_id, is_deleted);

-- Now add the deferred FK from departments.head_faculty_id → faculty
ALTER TABLE departments
    ADD CONSTRAINT fk_departments_head_faculty
    FOREIGN KEY (head_faculty_id) REFERENCES faculty(faculty_id);

-- And batches.class_teacher_id → faculty
ALTER TABLE batches
    ADD CONSTRAINT fk_batches_class_teacher
    FOREIGN KEY (class_teacher_id) REFERENCES faculty(faculty_id);

-- ---------------------------------------------------------------------------
-- 2.8  faculty_education
-- ---------------------------------------------------------------------------
CREATE TABLE faculty_education (
    faculty_education_id     BIGSERIAL       PRIMARY KEY,
    faculty_education_public_id UUID         NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    faculty_id               BIGINT          NOT NULL REFERENCES faculty(faculty_id),
    degree                   VARCHAR(100)    NOT NULL,               -- B.Tech | M.Tech | Ph.D
    field_of_study           VARCHAR(200)    NOT NULL,
    institution              VARCHAR(300)    NOT NULL,
    passing_year             SMALLINT        NOT NULL,
    grade_or_percentage      VARCHAR(20),
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_faculty_education_public_id UNIQUE (faculty_education_public_id)
);

CREATE INDEX idx_faculty_education_faculty ON faculty_education (tenant_id, faculty_id, is_deleted);

-- ---------------------------------------------------------------------------
-- 2.9  faculty_work_experience
-- ---------------------------------------------------------------------------
CREATE TABLE faculty_work_experience (
    work_experience_id       BIGSERIAL       PRIMARY KEY,
    work_experience_public_id UUID           NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    faculty_id               BIGINT          NOT NULL REFERENCES faculty(faculty_id),
    organization             VARCHAR(300)    NOT NULL,
    role                     VARCHAR(200)    NOT NULL,
    from_date                DATE            NOT NULL,
    to_date                  DATE,                                   -- NULL = current
    description              TEXT,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_work_experience_public_id UNIQUE (work_experience_public_id),
    CONSTRAINT chk_work_experience_dates    CHECK (to_date IS NULL OR to_date > from_date)
);

CREATE INDEX idx_work_exp_faculty ON faculty_work_experience (tenant_id, faculty_id, is_deleted);

-- ---------------------------------------------------------------------------
-- 2.10  course_assignments  (faculty + course + batch + semester)
-- ---------------------------------------------------------------------------
CREATE TABLE course_assignments (
    course_assignment_id     BIGSERIAL       PRIMARY KEY,
    course_assignment_public_id UUID         NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    course_id                BIGINT          NOT NULL REFERENCES courses(course_id),
    faculty_id               BIGINT          NOT NULL REFERENCES faculty(faculty_id),
    batch_id                 BIGINT          NOT NULL REFERENCES batches(batch_id),
    academic_year_id         BIGINT          NOT NULL REFERENCES academic_years(academic_year_id),
    semester_number          SMALLINT        NOT NULL,
    section                  VARCHAR(10),                            -- A | B | C
    is_primary               BOOLEAN         NOT NULL DEFAULT TRUE,  -- co-teachers can be non-primary
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_course_assignments_public_id UNIQUE (course_assignment_public_id)
);

CREATE UNIQUE INDEX uq_course_assignment_unique
    ON course_assignments (tenant_id, course_id, faculty_id, batch_id, academic_year_id, semester_number, COALESCE(section,''))
    WHERE is_deleted = FALSE;

CREATE INDEX idx_ca_faculty       ON course_assignments (tenant_id, faculty_id, is_deleted);
CREATE INDEX idx_ca_batch         ON course_assignments (tenant_id, batch_id, is_deleted);
CREATE INDEX idx_ca_course        ON course_assignments (tenant_id, course_id, is_deleted);


-- =============================================================================
-- GROUP 3 — TIMETABLE & ROOMS
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 3.1  rooms
-- ---------------------------------------------------------------------------
CREATE TABLE rooms (
    room_id                  BIGSERIAL       PRIMARY KEY,
    room_public_id           UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    building                 VARCHAR(100)    NOT NULL,
    floor                    SMALLINT,
    room_number              VARCHAR(20)     NOT NULL,
    name                     VARCHAR(100),                           -- "Main Lab" | "Seminar Hall A"
    capacity                 INT             NOT NULL,
    room_type                VARCHAR(50)     NOT NULL DEFAULT 'classroom',-- classroom | lab | seminar | auditorium
    has_projector            BOOLEAN         NOT NULL DEFAULT FALSE,
    has_ac                   BOOLEAN         NOT NULL DEFAULT FALSE,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_rooms_public_id UNIQUE (room_public_id)
);

CREATE UNIQUE INDEX uq_rooms_tenant_building_room
    ON rooms (tenant_id, building, room_number)
    WHERE is_deleted = FALSE;

-- ---------------------------------------------------------------------------
-- 3.2  timetable_slots  (master weekly schedule)
-- ---------------------------------------------------------------------------
CREATE TABLE timetable_slots (
    slot_id                  BIGSERIAL       PRIMARY KEY,
    slot_public_id           UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    course_assignment_id     BIGINT          NOT NULL REFERENCES course_assignments(course_assignment_id),
    room_id                  BIGINT          NOT NULL REFERENCES rooms(room_id),
    day_of_week              SMALLINT        NOT NULL CHECK (day_of_week BETWEEN 1 AND 7),-- 1=Mon … 7=Sun
    start_time               TIME            NOT NULL,
    end_time                 TIME            NOT NULL,
    slot_type                VARCHAR(30)     NOT NULL DEFAULT 'lecture',-- lecture | lab | tutorial
    effective_from           DATE            NOT NULL,
    effective_to             DATE,
    recurrence               VARCHAR(20)     NOT NULL DEFAULT 'weekly',-- weekly | biweekly | once
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_timetable_slots_public_id UNIQUE (slot_public_id),
    CONSTRAINT chk_slot_times               CHECK (end_time > start_time)
);

-- Room cannot be double-booked in the same slot
CREATE UNIQUE INDEX uq_timetable_room_slot
    ON timetable_slots (tenant_id, room_id, day_of_week, start_time, effective_from)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_timetable_ca       ON timetable_slots (tenant_id, course_assignment_id, is_deleted);
CREATE INDEX idx_timetable_day_room ON timetable_slots (tenant_id, day_of_week, room_id);


-- =============================================================================
-- GROUP 4 — ATTENDANCE
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 4.1  attendance_sessions  (one actual class conducted)
-- ---------------------------------------------------------------------------
CREATE TABLE attendance_sessions (
    session_id               BIGSERIAL       PRIMARY KEY,
    session_public_id        UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    course_assignment_id     BIGINT          NOT NULL REFERENCES course_assignments(course_assignment_id),
    slot_id                  BIGINT          REFERENCES timetable_slots(slot_id),
    room_id                  BIGINT          REFERENCES rooms(room_id),
    session_date             DATE            NOT NULL,
    start_time               TIME            NOT NULL,
    end_time                 TIME            NOT NULL,
    topic_covered            TEXT,
    session_type             VARCHAR(30)     NOT NULL DEFAULT 'lecture',-- lecture | lab | tutorial | extra
    conducted_by             BIGINT          NOT NULL REFERENCES faculty(faculty_id),
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_attendance_sessions_public_id UNIQUE (session_public_id),
    CONSTRAINT chk_session_times                CHECK (end_time > start_time)
);

CREATE INDEX idx_as_ca_date ON attendance_sessions (tenant_id, course_assignment_id, session_date, is_deleted);
CREATE INDEX idx_as_date     ON attendance_sessions (tenant_id, session_date, is_deleted);

-- ---------------------------------------------------------------------------
-- 4.2  attendance_records  (one row per student per session)
-- ---------------------------------------------------------------------------
CREATE TABLE attendance_records (
    attendance_record_id     BIGSERIAL       PRIMARY KEY,
    attendance_record_public_id UUID         NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    session_id               BIGINT          NOT NULL REFERENCES attendance_sessions(session_id),
    student_id               BIGINT          NOT NULL REFERENCES students(student_id),
    status                   VARCHAR(20)     NOT NULL DEFAULT 'absent',-- present | absent | late | excused
    marked_by                BIGINT          NOT NULL REFERENCES users(user_id),
    marked_at                TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    remarks                  TEXT,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_attendance_records_public_id    UNIQUE (attendance_record_public_id)
);

-- One record per student per session
CREATE UNIQUE INDEX uq_attendance_records_session_student
    ON attendance_records (tenant_id, session_id, student_id)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_ar_student ON attendance_records (tenant_id, student_id, is_deleted);
CREATE INDEX idx_ar_session ON attendance_records (tenant_id, session_id, is_deleted);


-- =============================================================================
-- GROUP 5 — EXAMS & MARKS
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 5.1  exams
-- ---------------------------------------------------------------------------
CREATE TABLE exams (
    exam_id                  BIGSERIAL       PRIMARY KEY,
    exam_public_id           UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    academic_year_id         BIGINT          NOT NULL REFERENCES academic_years(academic_year_id),
    batch_id                 BIGINT          NOT NULL REFERENCES batches(batch_id),
    name                     VARCHAR(200)    NOT NULL,               -- Mid-Semester I | End-Semester
    exam_type                VARCHAR(50)     NOT NULL,               -- internal | external | practical | viva
    semester_number          SMALLINT        NOT NULL,
    start_date               DATE            NOT NULL,
    end_date                 DATE            NOT NULL,
    total_marks              NUMERIC(6,2)    NOT NULL,
    passing_marks            NUMERIC(6,2)    NOT NULL,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_exams_public_id  UNIQUE (exam_public_id),
    CONSTRAINT chk_exam_dates      CHECK (end_date >= start_date),
    CONSTRAINT chk_passing_marks   CHECK (passing_marks <= total_marks)
);

CREATE INDEX idx_exams_batch ON exams (tenant_id, batch_id, is_deleted);
CREATE INDEX idx_exams_year  ON exams (tenant_id, academic_year_id, is_deleted);

-- ---------------------------------------------------------------------------
-- 5.2  exam_schedules  (per-course exam details)
-- ---------------------------------------------------------------------------
CREATE TABLE exam_schedules (
    exam_schedule_id         BIGSERIAL       PRIMARY KEY,
    exam_schedule_public_id  UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    exam_id                  BIGINT          NOT NULL REFERENCES exams(exam_id),
    course_id                BIGINT          NOT NULL REFERENCES courses(course_id),
    room_id                  BIGINT          NOT NULL REFERENCES rooms(room_id),
    exam_date                DATE            NOT NULL,
    start_time               TIME            NOT NULL,
    end_time                 TIME            NOT NULL,
    invigilator_id           BIGINT          REFERENCES faculty(faculty_id),
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_exam_schedules_public_id    UNIQUE (exam_schedule_public_id),
    CONSTRAINT chk_exam_schedule_times        CHECK (end_time > start_time)
);

CREATE UNIQUE INDEX uq_exam_schedule_exam_course
    ON exam_schedules (tenant_id, exam_id, course_id)
    WHERE is_deleted = FALSE;

-- Room cannot be double-booked on same date+time for exams
CREATE UNIQUE INDEX uq_exam_schedule_room_datetime
    ON exam_schedules (tenant_id, room_id, exam_date, start_time)
    WHERE is_deleted = FALSE;

-- ---------------------------------------------------------------------------
-- 5.3  marks  (student marks per exam per course)
-- ---------------------------------------------------------------------------
CREATE TABLE marks (
    mark_id                  BIGSERIAL       PRIMARY KEY,
    mark_public_id           UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    exam_id                  BIGINT          NOT NULL REFERENCES exams(exam_id),
    student_id               BIGINT          NOT NULL REFERENCES students(student_id),
    course_id                BIGINT          NOT NULL REFERENCES courses(course_id),
    marks_obtained           NUMERIC(6,2)    NOT NULL,
    total_marks              NUMERIC(6,2)    NOT NULL,
    grade                    VARCHAR(5),                             -- A+ | A | B+ | B | C | D | F
    grade_points             NUMERIC(4,2),
    is_absent                BOOLEAN         NOT NULL DEFAULT FALSE,
    is_withheld              BOOLEAN         NOT NULL DEFAULT FALSE,
    remarks                  TEXT,
    entered_by               BIGINT          NOT NULL REFERENCES users(user_id),
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_marks_public_id           UNIQUE (mark_public_id),
    CONSTRAINT chk_marks_obtained           CHECK (marks_obtained <= total_marks),
    CONSTRAINT chk_marks_non_negative       CHECK (marks_obtained >= 0)
);

CREATE UNIQUE INDEX uq_marks_exam_student_course
    ON marks (tenant_id, exam_id, student_id, course_id)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_marks_student ON marks (tenant_id, student_id, is_deleted);
CREATE INDEX idx_marks_exam    ON marks (tenant_id, exam_id, is_deleted);


-- =============================================================================
-- GROUP 6 — FINANCE
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 6.1  fee_structures
-- ---------------------------------------------------------------------------
CREATE TABLE fee_structures (
    fee_structure_id         BIGSERIAL       PRIMARY KEY,
    fee_structure_public_id  UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    program_id               BIGINT          NOT NULL REFERENCES programs(program_id),
    academic_year_id         BIGINT          NOT NULL REFERENCES academic_years(academic_year_id),
    semester_number          SMALLINT,                               -- NULL = annual fee
    category                 VARCHAR(50),                            -- general | SC | ST | OBC | NULL = all
    fee_name                 VARCHAR(200)    NOT NULL,               -- Tuition Fee | Hostel Fee
    total_amount             NUMERIC(12,2)   NOT NULL,
    due_date                 DATE            NOT NULL,
    late_fee_per_day         NUMERIC(8,2)    NOT NULL DEFAULT 0,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_fee_structures_public_id UNIQUE (fee_structure_public_id),
    CONSTRAINT chk_fee_amount              CHECK (total_amount > 0)
);

CREATE INDEX idx_fee_structures_program ON fee_structures (tenant_id, program_id, academic_year_id, is_deleted);

-- ---------------------------------------------------------------------------
-- 6.2  fee_invoices  (bill raised per student)
-- ---------------------------------------------------------------------------
CREATE TABLE fee_invoices (
    invoice_id               BIGSERIAL       PRIMARY KEY,
    invoice_public_id        UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    student_id               BIGINT          NOT NULL REFERENCES students(student_id),
    fee_structure_id         BIGINT          NOT NULL REFERENCES fee_structures(fee_structure_id),
    invoice_number           VARCHAR(50)     NOT NULL,
    issue_date               DATE            NOT NULL,
    due_date                 DATE            NOT NULL,
    subtotal                 NUMERIC(12,2)   NOT NULL,
    discount                 NUMERIC(12,2)   NOT NULL DEFAULT 0,
    late_fee                 NUMERIC(12,2)   NOT NULL DEFAULT 0,
    total_amount             NUMERIC(12,2)   NOT NULL,
    status                   VARCHAR(20)     NOT NULL DEFAULT 'draft',-- draft | sent | partially_paid | paid | overdue | cancelled
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_fee_invoices_public_id UNIQUE (invoice_public_id)
);

CREATE UNIQUE INDEX uq_fee_invoices_number
    ON fee_invoices (tenant_id, invoice_number)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_fee_invoices_student ON fee_invoices (tenant_id, student_id, is_deleted);
CREATE INDEX idx_fee_invoices_status  ON fee_invoices (tenant_id, status, due_date);

-- ---------------------------------------------------------------------------
-- 6.3  fee_payments  (actual payment transactions)
-- ---------------------------------------------------------------------------
CREATE TABLE fee_payments (
    payment_id               BIGSERIAL       PRIMARY KEY,
    payment_public_id        UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    invoice_id               BIGINT          NOT NULL REFERENCES fee_invoices(invoice_id),
    student_id               BIGINT          NOT NULL REFERENCES students(student_id),
    amount_paid              NUMERIC(12,2)   NOT NULL,
    payment_date             DATE            NOT NULL,
    payment_mode             VARCHAR(40)     NOT NULL,               -- cash | card | upi | netbanking | cheque | dd
    transaction_reference    VARCHAR(100),
    gateway_response         JSONB,                                  -- raw payload from payment gateway
    receipt_number           VARCHAR(50)     NOT NULL,
    collected_by             BIGINT          REFERENCES users(user_id),
    status                   VARCHAR(20)     NOT NULL DEFAULT 'pending',-- pending | success | failed | refunded
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_fee_payments_public_id UNIQUE (payment_public_id),
    CONSTRAINT chk_payment_amount        CHECK (amount_paid > 0)
);

CREATE UNIQUE INDEX uq_fee_payments_receipt
    ON fee_payments (tenant_id, receipt_number)
    WHERE is_deleted = FALSE;

CREATE INDEX idx_fee_payments_invoice ON fee_payments (tenant_id, invoice_id, is_deleted);
CREATE INDEX idx_fee_payments_student ON fee_payments (tenant_id, student_id, is_deleted);
CREATE INDEX idx_fee_payments_date    ON fee_payments (tenant_id, payment_date DESC);


-- =============================================================================
-- GROUP 7 — SUPPORT TABLES
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 7.1  holidays
-- ---------------------------------------------------------------------------
CREATE TABLE holidays (
    holiday_id               BIGSERIAL       PRIMARY KEY,
    holiday_public_id        UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    title                    VARCHAR(200)    NOT NULL,
    holiday_date             DATE            NOT NULL,
    holiday_type             VARCHAR(50)     NOT NULL DEFAULT 'national',-- national | state | college | optional
    applies_to               VARCHAR(50)     NOT NULL DEFAULT 'all',-- all | students | faculty
    description              TEXT,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_holidays_public_id UNIQUE (holiday_public_id)
);

CREATE INDEX idx_holidays_tenant_date ON holidays (tenant_id, holiday_date, is_deleted);

-- ---------------------------------------------------------------------------
-- 7.2  announcements
-- ---------------------------------------------------------------------------
CREATE TABLE announcements (
    announcement_id          BIGSERIAL       PRIMARY KEY,
    announcement_public_id   UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    title                    VARCHAR(300)    NOT NULL,
    content                  TEXT            NOT NULL,
    audience                 VARCHAR(50)     NOT NULL DEFAULT 'all',-- all | students | faculty | batch | department
    audience_ref_id          BIGINT,                                 -- batch_id or department_id
    priority                 VARCHAR(20)     NOT NULL DEFAULT 'normal',-- low | normal | high | urgent
    pinned_until             DATE,
    published_at             TIMESTAMPTZ,
    expires_at               TIMESTAMPTZ,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_announcements_public_id UNIQUE (announcement_public_id)
);

CREATE INDEX idx_announcements_tenant ON announcements (tenant_id, published_at DESC, is_deleted);

-- ---------------------------------------------------------------------------
-- 7.3  leave_applications
-- ---------------------------------------------------------------------------
CREATE TABLE leave_applications (
    leave_id                 BIGSERIAL       PRIMARY KEY,
    leave_public_id          UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    applicant_user_id        BIGINT          NOT NULL REFERENCES users(user_id),
    applicant_type           VARCHAR(20)     NOT NULL,               -- student | faculty
    leave_type               VARCHAR(50)     NOT NULL,               -- medical | personal | academic | emergency
    from_date                DATE            NOT NULL,
    to_date                  DATE            NOT NULL,
    reason                   TEXT            NOT NULL,
    document_url             TEXT,
    status                   VARCHAR(20)     NOT NULL DEFAULT 'pending',-- pending | approved | rejected | cancelled
    reviewed_by              BIGINT          REFERENCES users(user_id),
    reviewed_at              TIMESTAMPTZ,
    reviewer_remarks         TEXT,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_leave_applications_public_id UNIQUE (leave_public_id),
    CONSTRAINT chk_leave_dates                 CHECK (to_date >= from_date)
);

CREATE INDEX idx_leave_applicant ON leave_applications (tenant_id, applicant_user_id, is_deleted);
CREATE INDEX idx_leave_status    ON leave_applications (tenant_id, status, is_deleted);

-- ---------------------------------------------------------------------------
-- 7.4  user_sessions  (JWT refresh token store)
-- ---------------------------------------------------------------------------
CREATE TABLE user_sessions (
    session_id               BIGSERIAL       PRIMARY KEY,
    session_public_id        UUID            NOT NULL DEFAULT uuid_generate_v4(),
    tenant_id                BIGINT          NOT NULL REFERENCES tenants(tenant_id),
    user_id                  BIGINT          NOT NULL REFERENCES users(user_id),
    refresh_token_hash       TEXT            NOT NULL,
    device_info              JSONB,
    ip_address               INET,
    expires_at               TIMESTAMPTZ     NOT NULL,
    revoked_at               TIMESTAMPTZ,
    -- audit
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by               BIGINT          REFERENCES users(user_id),
    updated_at               TIMESTAMPTZ,
    updated_by               BIGINT          REFERENCES users(user_id),
    deleted_at               TIMESTAMPTZ,
    deleted_by               BIGINT          REFERENCES users(user_id),
    is_deleted               BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_user_sessions_public_id UNIQUE (session_public_id),
    CONSTRAINT uq_user_sessions_token     UNIQUE (refresh_token_hash)
);

CREATE INDEX idx_user_sessions_user    ON user_sessions (tenant_id, user_id, is_deleted);
CREATE INDEX idx_user_sessions_expires ON user_sessions (expires_at) WHERE revoked_at IS NULL;


-- =============================================================================
-- GROUP 8 — VIEWS (commonly used aggregations)
-- =============================================================================

-- Attendance percentage per student per course assignment
CREATE OR REPLACE VIEW v_attendance_summary AS
SELECT
    ar.tenant_id,
    ar.student_id,
    ase.course_assignment_id,
    COUNT(*)                                                    AS total_sessions,
    COUNT(*) FILTER (WHERE ar.status = 'present')               AS present_count,
    COUNT(*) FILTER (WHERE ar.status = 'absent')                AS absent_count,
    COUNT(*) FILTER (WHERE ar.status = 'late')                  AS late_count,
    ROUND(
        COUNT(*) FILTER (WHERE ar.status IN ('present','late'))::NUMERIC
        / NULLIF(COUNT(*), 0) * 100, 2
    )                                                           AS attendance_pct
FROM attendance_records ar
JOIN attendance_sessions ase ON ase.session_id = ar.session_id
WHERE ar.is_deleted  = FALSE
  AND ase.is_deleted = FALSE
GROUP BY ar.tenant_id, ar.student_id, ase.course_assignment_id;

-- Student GPA per exam
CREATE OR REPLACE VIEW v_student_gpa AS
SELECT
    m.tenant_id,
    m.student_id,
    m.exam_id,
    ROUND(AVG(m.grade_points), 2)   AS gpa,
    SUM(m.marks_obtained)           AS total_obtained,
    SUM(m.total_marks)              AS total_possible
FROM marks m
WHERE m.is_deleted  = FALSE
  AND m.is_absent   = FALSE
  AND m.is_withheld = FALSE
GROUP BY m.tenant_id, m.student_id, m.exam_id;

-- Outstanding fee per student
CREATE OR REPLACE VIEW v_fee_outstanding AS
SELECT
    fi.tenant_id,
    fi.student_id,
    COUNT(fi.invoice_id)                         AS total_invoices,
    SUM(fi.total_amount)                         AS total_billed,
    COALESCE(SUM(fp.amount_paid), 0)             AS total_paid,
    SUM(fi.total_amount) - COALESCE(SUM(fp.amount_paid), 0) AS outstanding
FROM fee_invoices fi
LEFT JOIN fee_payments fp
       ON fp.invoice_id = fi.invoice_id
      AND fp.status      = 'success'
      AND fp.is_deleted  = FALSE
WHERE fi.is_deleted = FALSE
  AND fi.status    != 'cancelled'
GROUP BY fi.tenant_id, fi.student_id;


-- =============================================================================
-- END OF SCHEMA
-- =============================================================================
