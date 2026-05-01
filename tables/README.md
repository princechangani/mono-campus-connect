# CampusConnect — Database Setup

## Files

| File | Purpose |
|------|---------|
| `01_postgres_schema.sql` | Full PostgreSQL schema — 31 tables, indexes, views |
| `02_mongodb_schema.js`   | MongoDB collections, indexes, JSON Schema validators |
| `03_seed_data.sql`       | Development seed data (2 tenants, sample students/faculty/marks) |

---

## PostgreSQL Setup

```bash
# Create database
createdb campusconnect

# Apply schema
psql -d campusconnect -f 01_postgres_schema.sql

# Apply seed data (optional — dev only)
psql -d campusconnect -f 03_seed_data.sql
```

Minimum PostgreSQL version: **14**  
Required extensions (auto-installed by schema): `uuid-ossp`, `pgcrypto`, `citext`

---

## MongoDB Setup

```bash
# Run against your MongoDB instance
mongosh "mongodb://localhost:27017/campusconnect" 02_mongodb_schema.js
```

Minimum MongoDB version: **6.0**

---

## Table count summary (PostgreSQL)

| Group | Tables |
|-------|--------|
| Tenant & identity | tenants, users, departments, roles, permissions, role_permissions, user_roles |
| Academic domain   | academic_years, programs, courses, program_courses, batches, students, faculty, faculty_education, faculty_work_experience, course_assignments |
| Timetable & rooms | rooms, timetable_slots |
| Attendance        | attendance_sessions, attendance_records |
| Exams & marks     | exams, exam_schedules, marks |
| Finance           | fee_structures, fee_invoices, fee_payments |
| Support           | holidays, announcements, leave_applications, user_sessions |

## Collection summary (MongoDB)

| Collection | Purpose | TTL |
|-----------|---------|-----|
| study_materials   | Files/notes uploaded by faculty | — |
| notifications     | Push/email/SMS alerts | 90 days (sent) |
| audit_logs        | Immutable write trail | Never |
| event_logs        | Clickstream analytics | 90 days |
| chat_messages     | Internal messaging | — |
| report_snapshots  | Cached heavy reports | 24 hours |

---

## Multitenancy notes

- Every PostgreSQL row has `tenant_id BIGINT NOT NULL` referencing `tenants(tenant_id)`
- Unique constraints are scoped per tenant via partial indexes: `WHERE is_deleted = FALSE`
- `{table}_public_id UUID` is the **only** ID exposed in APIs — internal bigint PKs stay private
- Redis cache keys should be prefixed: `tenant:{tenant_public_id}:...`
- MongoDB documents always include `tenant_id` (UUID string matching `tenants.tenant_public_id`)

## Soft delete pattern

Every table supports soft deletes:
```sql
-- Soft delete a record
UPDATE students
SET is_deleted = TRUE, deleted_at = NOW(), deleted_by = :actor_user_id
WHERE student_id = :id AND tenant_id = :tenant_id;

-- All queries should filter
WHERE is_deleted = FALSE AND tenant_id = :tenant_id
```
