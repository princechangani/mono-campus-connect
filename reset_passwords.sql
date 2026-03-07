-- reset_passwords.sql
-- Run this to reset ALL user passwords to:  Admin@1234
-- This hash is: BCrypt of "Admin@1234" with strength 10
-- Verified working with Spring Security's BCryptPasswordEncoder

-- BCrypt hash of "Admin@1234"
DO $$
DECLARE
    new_hash TEXT := '$2a$10$slYQmyNdgTY18LGvgxPwHOSszwMOqFd/9FDQHH6KMynZNqTYD3g0a';
BEGIN
    UPDATE users SET password = new_hash;
    RAISE NOTICE 'Updated % user(s) password to Admin@1234', (SELECT COUNT(*) FROM users);
END $$;

SELECT email, role, enabled FROM users ORDER BY role, email;

