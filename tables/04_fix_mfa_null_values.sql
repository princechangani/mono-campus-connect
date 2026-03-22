-- Migration: Fix NULL values in mfa_enabled column
-- This script updates any NULL values in the mfa_enabled column to FALSE

UPDATE users
SET mfa_enabled = FALSE
WHERE mfa_enabled IS NULL;

-- Verify the update
SELECT COUNT(*) as users_with_null_mfa,
       (SELECT COUNT(*) FROM users) as total_users
FROM users
WHERE mfa_enabled IS NULL;

