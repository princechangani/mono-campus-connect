# CampusConnect SaaS Implementation Plan

## 1) Product Direction

### Vision
Build CampusConnect as a full multi-tenant SaaS platform where colleges and universities can manage academic and administrative workflows in one system.

### Problem Statement
Managing student details, attendance, timetables, marks, faculty workflows, and admin operations is difficult when data is fragmented and processes are manual. CampusConnect solves this with centralized records, role-based access, and streamlined communication.

### One-line Value Proposition
CampusConnect simplifies academic and administrative tasks for students, faculty, and admins with a scalable, secure, and tenant-safe architecture.

## 2) Scope and Roles

### Student Interface
- View profile and academic details
- Track attendance
- View timetable
- View marks/results
- Access study materials
- Receive notifications and announcements

### Faculty Interface
- Manage profile, education, and work experience
- Manage assigned courses and batches
- Upload study materials
- Conduct attendance sessions and mark attendance
- Manage exam marks
- View timetable and student details

### Admin Interface
- Manage departments, programs, courses, rooms, batches
- Add/update/remove student and faculty records
- Manage timetable, exams, announcements, holidays, leaves
- Manage fee structures, invoices, and payments
- Monitor operational dashboards and reports

### Super Admin (SaaS Control Plane)
- Onboard and manage tenants
- Manage subscription plans and limits
- Monitor tenant usage, health, and billing lifecycle
- Manage global support and platform configuration

## 3) Data Foundation (Already Designed)

### PostgreSQL (Canonical transactional system)
Source: `tables/01_postgres_schema.sql`

- Group 1 Tenant and Identity (7 tables)
  - `tenants`, `users`, `departments`, `roles`, `permissions`, `role_permissions`, `user_roles`
- Group 2 Academic Domain (10 tables)
  - `academic_years`, `programs`, `courses`, `program_courses`, `batches`, `students`, `faculty`, `faculty_education`, `faculty_work_experience`, `course_assignments`
- Group 3 Timetable and Rooms (2 tables)
  - `rooms`, `timetable_slots`
- Group 4 Attendance (2 tables)
  - `attendance_sessions`, `attendance_records`
- Group 5 Exams and Marks (3 tables)
  - `exams`, `exam_schedules`, `marks`
- Group 6 Finance (3 tables)
  - `fee_structures`, `fee_invoices`, `fee_payments`
- Group 7 Support (4 tables)
  - `holidays`, `announcements`, `leave_applications`, `user_sessions`
- Group 8 Views (3 views)
  - `v_attendance_summary`, `v_student_gpa`, `v_fee_outstanding`

### MongoDB (Document/event modules)
Source: `tables/02_mongodb_schema.js`

- `study_materials`
- `notifications`
- `audit_logs`
- `event_logs`
- `chat_messages`
- `report_snapshots`

## 4) Module-to-Data Mapping

### Student module
- PostgreSQL: `users`, `students`, `batches`, `programs`, `courses`, `program_courses`, `attendance_sessions`, `attendance_records`, `timetable_slots`, `exams`, `exam_schedules`, `marks`, `announcements`, `fee_invoices`, `fee_payments`
- MongoDB: `study_materials`, `notifications`, `chat_messages`, `report_snapshots`

### Faculty module
- PostgreSQL: `users`, `faculty`, `faculty_education`, `faculty_work_experience`, `course_assignments`, `attendance_sessions`, `attendance_records`, `timetable_slots`, `exams`, `exam_schedules`, `marks`, `leave_applications`
- MongoDB: `study_materials`, `notifications`, `chat_messages`, `audit_logs`

### Admin module
- PostgreSQL: `departments`, `academic_years`, `programs`, `courses`, `program_courses`, `batches`, `students`, `faculty`, `rooms`, `timetable_slots`, `exams`, `exam_schedules`, `marks`, `fee_structures`, `fee_invoices`, `fee_payments`, `holidays`, `announcements`, `leave_applications`
- MongoDB: `notifications`, `audit_logs`, `event_logs`, `report_snapshots`

### Super-admin module
- PostgreSQL: `tenants`, `roles`, `permissions`, `role_permissions`, `user_roles`, tenant usage columns (`max_students`, `max_faculty`, `plan`, `subscription_expires_at`)
- MongoDB: `audit_logs`, `event_logs`, `report_snapshots`

## 5) Delivery Roadmap (16 Weeks)

## Phase 0 - Foundation and Alignment (Week 1)
- [ ] Freeze schema contract from `tables/01_postgres_schema.sql` and `tables/02_mongodb_schema.js`
- [ ] Finalize domain glossary and API naming standards
- [ ] Confirm tenant resolution strategy (subdomain/header/custom domain)
- [ ] Define role-permission matrix and context-scoped roles
- [ ] Publish release plan and environments plan

## Phase 1 - Platform Core (Weeks 2-4)
- [ ] Tenant onboarding APIs and bootstrap workflow
- [ ] Auth: login/register/refresh/logout/forgot-reset password
- [ ] RBAC with `roles`, `permissions`, `role_permissions`, `user_roles`
- [ ] Tenant context enforcement in all requests
- [ ] Soft delete defaults (`is_deleted = false`) in repository layer
- [ ] Session security with `user_sessions`

## Phase 2 - Academic Master Data (Weeks 5-7)
- [ ] Departments CRUD
- [ ] Academic years CRUD with current-year constraints
- [ ] Programs and courses CRUD
- [ ] Program-course curriculum mapping
- [ ] Batches creation and progression workflow
- [ ] Student and faculty onboarding workflows
- [ ] Faculty profile enrichment (education and work experience)

## Phase 3 - Timetable, Attendance, Exams, Materials (Weeks 8-10)
- [ ] Course assignments (faculty-course-batch-semester)
- [ ] Rooms and timetable slot scheduling with collision checks
- [ ] Attendance session creation and attendance records
- [ ] Exam creation, per-course scheduling, marks entry
- [ ] Material upload/list/filter flows using `study_materials`
- [ ] Notification delivery workflow using `notifications`

## Phase 4 - Admin Ops and Finance (Weeks 11-12)
- [ ] Fee structure templates by program/year/category
- [ ] Invoice generation and status lifecycle
- [ ] Payment records and receipt tracking
- [ ] Holidays, announcements, leave applications workflows
- [ ] Dashboard endpoints from SQL views and cached snapshots

## Phase 5 - SaaS Control Plane (Weeks 13-14)
- [ ] Super-admin tenant list, status, plan management
- [ ] Tenant usage tracking and limits enforcement
- [ ] Tenant suspension/reactivation and billing state hooks
- [ ] Cross-tenant operational dashboards (strictly super-admin only)

## Phase 6 - Hardening and Launch (Weeks 15-16)
- [ ] Load/performance tests for peak attendance and marks windows
- [ ] Security hardening and penetration test fixes
- [ ] Backup and disaster recovery drill
- [ ] UAT sign-off for student/faculty/admin/super-admin personas
- [ ] Production rollout and hypercare plan

## 6) Architecture for Scalability

### Multi-tenant model
- Shared-database shared-schema with strict `tenant_id` partitioning
- Every query must include tenant scope and active-row filter
- Public API IDs must use `*_public_id` UUID only

### Backend architecture
- Spring Boot modular monolith first, service-oriented boundaries from day one
- Suggested bounded contexts: `tenant`, `identity`, `academic`, `teaching`, `finance`, `support`, `reporting`
- Async workers for notifications, report generation, and heavy exports

### Data and caching
- PostgreSQL for strongly consistent transactions
- MongoDB for high-volume document/event workloads
- Redis for tenant-scoped cache/session/rate limit counters
- Cache key standard: `tenant:{tenant_public_id}:{module}:{key}`

### Horizontal scale strategy
- Stateless API nodes behind load balancer
- Separate worker deployment for background jobs
- Read replicas for analytical/readonly workloads
- Table partitioning strategy for high-growth tables if needed

## 7) Security and Compliance Baseline

- [ ] Secrets in vault/secret manager (no hardcoded secrets)
- [ ] JWT signing key rotation policy
- [ ] Password hashing and optional MFA support
- [ ] PII masking in logs and exports
- [ ] Immutable audit trail in MongoDB `audit_logs`
- [ ] Tenant isolation tests in CI
- [ ] Data retention policy for events, notifications, and snapshots

## 8) API and Contract Standards

- [ ] Version APIs as `/api/v1`
- [ ] Standard response envelope and error codes
- [ ] Pagination/filter/sort standards across list endpoints
- [ ] OpenAPI docs generated and validated in CI
- [ ] Idempotency key for sensitive create operations

## 9) Frontend Plan (Next.js)

- [ ] Role-based route groups for student, faculty, admin, super-admin
- [ ] Shared table/form/filter components to reduce duplication
- [ ] Central API client for auth refresh, tenant headers, error handling
- [ ] Dashboard cards and drill-down lists for each module
- [ ] Permission-driven UI actions (not role string checks only)

## 10) Testing and Quality Strategy

- [ ] Unit tests for services, validators, RBAC checks
- [ ] Repository tests for tenant filters and soft delete behavior
- [ ] Integration tests for end-to-end module workflows
- [ ] Contract tests between frontend and backend DTOs
- [ ] Performance tests for attendance, timetable, marks, notifications
- [ ] Security tests for cross-tenant data access attempts

## 11) CI/CD and Environments

- [ ] Environments: local, dev, staging, production
- [ ] Pipeline: lint -> test -> integration -> security scan -> build -> deploy
- [ ] Migration checks against ephemeral PostgreSQL and MongoDB in CI
- [ ] Blue/green or canary deployment with rollback criteria
- [ ] Post-deploy smoke tests and health checks

## 12) Implementation Backlog by Priority

### P0 (Must-have for first production)
- [ ] Tenant onboarding + identity + RBAC
- [ ] Academic masters (department/program/course/batch)
- [ ] Student and faculty management
- [ ] Timetable + attendance + marks
- [ ] Notifications + announcements

### P1 (High-value next)
- [ ] Finance (fee structures, invoices, payments)
- [ ] Leave workflows
- [ ] Reporting snapshots and operational dashboards
- [ ] Super-admin controls for tenant lifecycle

### P2 (Scale and product depth)
- [ ] Chat module hardening
- [ ] Advanced analytics and predictive alerts
- [ ] External integrations (LMS, payment gateways, SSO)

## 13) Definition of Done (Per Feature)

- [ ] API implemented with auth, tenant checks, validation, and audit logging
- [ ] UI screens implemented for relevant roles
- [ ] Unit and integration tests passing
- [ ] Observability added (logs, metrics, traces)
- [ ] Documentation updated (API + usage)
- [ ] Security checks completed

## 14) Major Risks and Mitigations

- Risk: cross-tenant data leak
  - Mitigation: mandatory tenant filters, test gates, code review checklist
- Risk: performance degradation at scale
  - Mitigation: index review, caching, async jobs, load tests before launch
- Risk: inconsistent contracts between frontend and backend
  - Mitigation: OpenAPI-first + contract tests in CI
- Risk: feature creep delays launch
  - Mitigation: strict P0/P1/P2 prioritization and phase gates

## 15) Go-Live Checklist

- [ ] Production tenant onboarding dry run complete
- [ ] Backup and restore runbook tested
- [ ] Alerts and dashboards configured
- [ ] Security baseline validated
- [ ] UAT signed off by all role personas
- [ ] Rollback plan documented and rehearsed

## 16) Immediate Next Actions (This Week)

- [ ] Confirm MVP scope (P0 only) for first release
- [ ] Freeze API conventions and module ownership
- [ ] Start Phase 1 tickets (tenant, auth, RBAC, tenant middleware)
- [ ] Stand up CI checks for tests + schema consistency
- [ ] Prepare staging dataset for realistic UAT

