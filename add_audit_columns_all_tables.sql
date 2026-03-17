-- ============================================================
-- add_audit_columns_all_tables.sql
--
-- Adds to every table in schema public:
--   1) <table_name>_public_id (UUID, NOT NULL, UNIQUE)
--   2) created_at, created_by, updated_at, updated_by,
--      deleted_at, deleted_by, is_deleted
--   3) Optional PK rename: id -> <table_name>_id
--
-- NOTE:
-- PK rename is ON by default as requested.
-- This can break existing JPA mappings until entity @Column names are updated.
-- ============================================================

CREATE EXTENSION IF NOT EXISTS pgcrypto;

DO $$
DECLARE
    t RECORD;
    v_public_col TEXT;
    v_public_uq_name TEXT;
    v_pk_name TEXT;
    v_pk_col_count INTEGER;
    v_has_id BOOLEAN;
    v_has_target_pk BOOLEAN;
    v_do_pk_rename BOOLEAN := TRUE; -- set FALSE if you want only additive columns
BEGIN
    FOR t IN
        SELECT tablename
        FROM pg_tables
        WHERE schemaname = 'public'
          AND tablename <> 'flyway_schema_history'
    LOOP
        v_public_col := t.tablename || '_public_id';
        v_public_uq_name := LEFT('uq_' || t.tablename || '_public_id', 63);

        -- 1) Add <table>_public_id
        EXECUTE format('ALTER TABLE public.%I ADD COLUMN IF NOT EXISTS %I UUID', t.tablename, v_public_col);
        EXECUTE format('UPDATE public.%I SET %I = gen_random_uuid() WHERE %I IS NULL', t.tablename, v_public_col, v_public_col);
        EXECUTE format('ALTER TABLE public.%I ALTER COLUMN %I SET NOT NULL', t.tablename, v_public_col);

        IF NOT EXISTS (
            SELECT 1
            FROM pg_constraint
            WHERE conname = v_public_uq_name
              AND conrelid = format('public.%I', t.tablename)::regclass
        ) THEN
            EXECUTE format('ALTER TABLE public.%I ADD CONSTRAINT %I UNIQUE (%I)', t.tablename, v_public_uq_name, v_public_col);
        END IF;

        -- 2) Add audit + soft-delete columns
        EXECUTE format('ALTER TABLE public.%I ADD COLUMN IF NOT EXISTS created_at TIMESTAMP', t.tablename);
        EXECUTE format('ALTER TABLE public.%I ADD COLUMN IF NOT EXISTS created_by BIGINT', t.tablename);
        EXECUTE format('ALTER TABLE public.%I ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP', t.tablename);
        EXECUTE format('ALTER TABLE public.%I ADD COLUMN IF NOT EXISTS updated_by BIGINT', t.tablename);
        EXECUTE format('ALTER TABLE public.%I ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP', t.tablename);
        EXECUTE format('ALTER TABLE public.%I ADD COLUMN IF NOT EXISTS deleted_by BIGINT', t.tablename);
        EXECUTE format('ALTER TABLE public.%I ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN', t.tablename);

        EXECUTE format('UPDATE public.%I SET created_at = NOW() WHERE created_at IS NULL', t.tablename);
        EXECUTE format('UPDATE public.%I SET updated_at = NOW() WHERE updated_at IS NULL', t.tablename);
        EXECUTE format('UPDATE public.%I SET is_deleted = FALSE WHERE is_deleted IS NULL', t.tablename);

        EXECUTE format('ALTER TABLE public.%I ALTER COLUMN created_at SET DEFAULT NOW()', t.tablename);
        EXECUTE format('ALTER TABLE public.%I ALTER COLUMN updated_at SET DEFAULT NOW()', t.tablename);
        EXECUTE format('ALTER TABLE public.%I ALTER COLUMN is_deleted SET DEFAULT FALSE', t.tablename);

        EXECUTE format('ALTER TABLE public.%I ALTER COLUMN created_at SET NOT NULL', t.tablename);
        EXECUTE format('ALTER TABLE public.%I ALTER COLUMN is_deleted SET NOT NULL', t.tablename);

        -- 3) Optional PK rename id -> <table>_id
        IF v_do_pk_rename THEN
            SELECT EXISTS (
                SELECT 1
                FROM information_schema.columns
                WHERE table_schema = 'public'
                  AND table_name = t.tablename
                  AND column_name = 'id'
            ) INTO v_has_id;

            SELECT EXISTS (
                SELECT 1
                FROM information_schema.columns
                WHERE table_schema = 'public'
                  AND table_name = t.tablename
                  AND column_name = t.tablename || '_id'
            ) INTO v_has_target_pk;

            IF v_has_id AND NOT v_has_target_pk THEN
                SELECT c.conname,
                       (SELECT COUNT(*) FROM unnest(c.conkey) k) AS col_count
                INTO v_pk_name, v_pk_col_count
                FROM pg_constraint c
                WHERE c.contype = 'p'
                  AND c.conrelid = format('public.%I', t.tablename)::regclass;

                IF v_pk_name IS NOT NULL AND v_pk_col_count = 1 THEN
                    EXECUTE format('ALTER TABLE public.%I RENAME COLUMN id TO %I', t.tablename, t.tablename || '_id');
                END IF;
            END IF;
        END IF;
    END LOOP;
END $$;

-- Quick verification summary
SELECT
    table_name,
    COUNT(*) FILTER (WHERE column_name IN (
        table_name || '_public_id',
        'created_at', 'created_by', 'updated_at', 'updated_by',
        'deleted_at', 'deleted_by', 'is_deleted'
    )) AS expected_columns_found
FROM information_schema.columns
WHERE table_schema = 'public'
GROUP BY table_name
ORDER BY table_name;

