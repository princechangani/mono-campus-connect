-- ============================================================
--  fix_schema.sql
--  Run this ONCE before seed.sql to align schema with RBAC
--  (roles + user_role_mapping many-to-many).
--
--  Usage:
--    psql -U postgres -d ccdb -f fix_schema.sql
--    psql -U postgres -d ccdb -f seed.sql
-- ============================================================

-- 1) Ensure RBAC tables exist
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_role_mapping (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    assigned_at TIMESTAMP,
    CONSTRAINT uk_user_role UNIQUE (user_id, role_id)
);

-- 2) Backfill roles and mappings from legacy users.role when available
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'users' AND column_name = 'role'
    ) THEN
        RAISE NOTICE 'Backfilling RBAC from users.role';

        INSERT INTO roles (role_name, description)
        SELECT DISTINCT u.role, u.role || ' role'
        FROM users u
        WHERE u.role IS NOT NULL
        ON CONFLICT (role_name) DO NOTHING;

        INSERT INTO user_role_mapping (user_id, role_id, assigned_at)
        SELECT u.id, r.id, NOW()
        FROM users u
        JOIN roles r ON r.role_name = u.role
        WHERE u.role IS NOT NULL
        ON CONFLICT (user_id, role_id) DO NOTHING;

        RAISE NOTICE 'Legacy users.role retained for compatibility. Drop it after seed/data migration.';
    ELSE
        RAISE NOTICE 'users.role already removed; skipping backfill';
    END IF;
END $$;

-- 3) Fix results.student_id when previously created as text
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name  = 'results'
          AND column_name = 'student_id'
          AND data_type   IN ('character varying','varchar','text')
    ) THEN
        RAISE NOTICE 'Fixing results.student_id column: varchar -> bigint';
        TRUNCATE TABLE results RESTART IDENTITY CASCADE;
        ALTER TABLE results DROP COLUMN student_id;
        ALTER TABLE results ADD COLUMN student_id BIGINT;
    ELSE
        RAISE NOTICE 'results.student_id is already correct type, no change needed.';
    END IF;
END $$;

-- 4) Verify RBAC/result columns
SELECT table_name, column_name, data_type
FROM information_schema.columns
WHERE (table_name = 'roles' AND column_name = 'role_name')
   OR (table_name = 'user_role_mapping' AND column_name IN ('user_id', 'role_id'))
   OR (table_name = 'results' AND column_name = 'student_id')
ORDER BY table_name, column_name;
