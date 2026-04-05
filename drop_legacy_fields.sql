-- Run this AFTER backfilling canonical IDs and deploying code that no longer uses legacy fields.
-- This removes redundant legacy course_code columns now derived from course_id.

ALTER TABLE exams DROP COLUMN IF EXISTS course_code;
ALTER TABLE attendance_records DROP COLUMN IF EXISTS course_code;
ALTER TABLE timetable_slots DROP COLUMN IF EXISTS course_code;
