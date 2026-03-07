-- ============================================================
--  fix_schema.sql
--  Run this ONCE before seed.sql to fix column types that
--  were created with the wrong type before @Enumerated and
--  Long fixes were applied.
--
--  Usage:
--    psql -U postgres -d ccdb -f fix_schema.sql
--    psql -U postgres -d ccdb -f seed.sql
-- ============================================================

-- 1. Fix users.role: drop smallint column, re-add as varchar
--    (Hibernate will keep it as varchar on next app start)
DO $$
BEGIN
    -- If role is smallint/integer, drop and re-add as varchar
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name  = 'users'
          AND column_name = 'role'
          AND data_type   IN ('smallint','integer','int2','int4')
    ) THEN
        RAISE NOTICE 'Fixing users.role column: smallint -> varchar(50)';
        TRUNCATE TABLE users CASCADE;   -- wipe dependent rows first
        ALTER TABLE users DROP COLUMN role;
        ALTER TABLE users ADD COLUMN role VARCHAR(50);
    ELSE
        RAISE NOTICE 'users.role is already correct type (%), no change needed.',
            (SELECT data_type FROM information_schema.columns
             WHERE table_name = 'users' AND column_name = 'role');
    END IF;
END $$;

-- 2. Fix results.student_id: drop varchar, re-add as bigint
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

-- 3. Verify final column types
SELECT
    table_name,
    column_name,
    data_type
FROM information_schema.columns
WHERE (table_name = 'users'   AND column_name = 'role')
   OR (table_name = 'results' AND column_name = 'student_id')
ORDER BY table_name, column_name;

