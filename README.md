# 🎓 Mono Campus Connect

> A multi-tenant SaaS platform for colleges and universities to manage academic and administrative workflows — built with **Spring Boot 3.4.5** + **Next.js** + **PostgreSQL** + **MongoDB**.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Database Setup](#database-setup)
- [Backend API Reference](#backend-api-reference)
- [Frontend Services & Hooks](#frontend-services--hooks)
- [User Roles & Routes](#user-roles--routes)
- [Test Accounts](#test-accounts)
- [Troubleshooting](#troubleshooting)
- [Roadmap](#roadmap)

---

## Overview

CampusConnect centralizes student details, attendance, timetables, marks, faculty workflows, fee management, and admin operations into one tenant-safe system with role-based access control.

### Key Stats

| Metric | Count |
|---|---|
| Backend Controllers | 41 |
| REST Endpoints | 120+ |
| Frontend Services | 24 |
| React Query Hooks | 20+ |
| TypeScript Interfaces | 30+ |
| Frontend Pages | 32 |

---

## Tech Stack

### Backend
- **Framework:** Spring Boot 3.4.5 (Java 21)
- **ORM:** Spring Data JPA + Spring Data MongoDB
- **Security:** Spring Security + JWT (jjwt 0.12.6)
- **Databases:** PostgreSQL (transactional) + MongoDB (documents/events)
- **API Docs:** SpringDoc OpenAPI (Swagger UI)
- **Mail:** Spring Boot Starter Mail (OTP delivery)
- **Storage:** AWS S3 SDK
- **Build:** Maven

### Frontend
- **Framework:** Next.js (App Router)
- **Language:** TypeScript
- **Styling:** Tailwind CSS
- **HTTP Client:** Axios
- **State:** React Query
- **Forms:** React Hook Form + Zod validation
- **UI Icons:** Lucide React
- **Notifications:** React Hot Toast

---

## Architecture

```
┌─────────────────────────────────────────────┐
│   React Components (Next.js App Router)     │
├─────────────────────────────────────────────┤
│   Custom Hooks (useApi.ts) — 20+ hooks      │
├─────────────────────────────────────────────┤
│   React Query (Caching & Sync)              │
├─────────────────────────────────────────────┤
│   API Client Factory (apiClient.ts)         │
├─────────────────────────────────────────────┤
│   Service Layer (24 services)               │
├─────────────────────────────────────────────┤
│   Axios Instance (api.ts)                   │
│   JWT injection, error handling             │
├─────────────────────────────────────────────┤
│   Backend APIs (Spring Boot) — 41 controllers│
│   120+ endpoints                            │
├─────────────────────────────────────────────┤
│   PostgreSQL & MongoDB                      │
└─────────────────────────────────────────────┘
```

### Multi-Tenancy
- Shared-database shared-schema with strict `tenant_id` partitioning
- Every query includes tenant scope and active-row filter
- Public API IDs use `*_public_id` UUID

---

## Project Structure

```
mono-campus-connect/
├── src/main/java/com/monocampusconnect/
│   ├── config/                    # Security, JWT, tenant context
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── JwtConfig.java
│   │   ├── PasswordEncoderConfig.java
│   │   ├── SecurityConfig.java
│   │   └── TenantContextHolder.java
│   ├── controller/
│   │   ├── AdminController.java, AuthController.java, ...  (14 core)
│   │   ├── postgres/              # 21 PostgreSQL CRUD controllers
│   │   │   ├── AcademicYearController.java
│   │   │   ├── StudentController.java
│   │   │   ├── FacultyController.java
│   │   │   └── ... (18 more)
│   │   └── mongo/                 # 5 MongoDB document controllers
│   │       ├── AuditLogDocumentController.java
│   │       ├── ChatMessageDocumentController.java
│   │       └── ReportSnapshotDocumentController.java
│   ├── dto/                       # Data Transfer Objects
│   │   ├── postgres/
│   │   └── mongo/
│   ├── model/                     # Entity models
│   │   ├── postgres/
│   │   └── mongo/
│   ├── repository/                # Data access layer
│   │   ├── postgres/
│   │   └── mongo/
│   ├── service/                   # Business logic
│   │   ├── postgres/
│   │   └── mongo/
│   ├── exception/                 # Custom exceptions
│   └── validator/                 # Input validation
├── frontend/
│   ├── src/
│   │   ├── app/                   # Next.js App Router pages
│   │   │   ├── (auth)/            # Login, Register, Forgot Password
│   │   │   ├── admin/             # 9 admin pages
│   │   │   ├── faculty/           # 8 faculty pages
│   │   │   ├── student/           # 8 student pages
│   │   │   ├── super-admin/       # Super admin dashboard
│   │   │   ├── notifications/
│   │   │   ├── profile/
│   │   │   └── layout.tsx
│   │   ├── components/
│   │   │   ├── ui/Common.tsx      # Alert, Button, Card, Input, etc.
│   │   │   └── layout/            # DashboardLayout, Sidebar
│   │   ├── hooks/useApi.ts        # 20+ React Query hooks
│   │   ├── lib/
│   │   │   ├── api.ts             # Axios instance
│   │   │   ├── apiClient.ts       # Organized API client factory
│   │   │   ├── auth.ts            # Auth utilities
│   │   │   └── services/          # 24 API service files + index.ts
│   │   └── types/models.ts        # 30+ TypeScript interfaces
│   ├── package.json
│   ├── tailwind.config.js
│   └── tsconfig.json
├── tables/                        # SQL scripts
│   ├── 01_postgres_schema.sql
│   ├── 02_mongodb_schema.js
│   ├── 03_seed_data.sql
│   └── 04_fix_mfa_null_values.sql
└── pom.xml
```

---

## Getting Started

### Prerequisites
- Java 21
- Maven
- Node.js 18+
- PostgreSQL
- MongoDB

### 1. Clone & Configure

```bash
# Configure environment
cp .env.example .env
# Set database credentials, JWT secret, mail config, etc.
```

### 2. Start Backend

```bash
# Build
mvn clean package

# Run
java -jar target/mono-campus-connect-0.0.1-SNAPSHOT.jar
# Backend runs on http://localhost:8081
```

### 3. Start Frontend

```bash
cd frontend
npm install
npm run dev
# Frontend runs on http://localhost:3000
# API calls proxy to http://localhost:8081/api
```

---

## Database Setup

### PostgreSQL Schema

```bash
createdb campus_connect -U postgres
psql -U postgres -d campus_connect -f tables/01_postgres_schema.sql
```

#### Schema Groups (34 tables + 3 views)

| Group | Tables |
|---|---|
| **Tenant & Identity** | `tenants`, `users`, `departments`, `roles`, `permissions`, `role_permissions`, `user_roles` |
| **Academic** | `academic_years`, `programs`, `courses`, `program_courses`, `batches`, `students`, `faculty`, `faculty_education`, `faculty_work_experience`, `course_assignments` |
| **Timetable & Rooms** | `rooms`, `timetable_slots` |
| **Attendance** | `attendance_sessions`, `attendance_records` |
| **Exams & Marks** | `exams`, `exam_schedules`, `marks` |
| **Finance** | `fee_structures`, `fee_invoices`, `fee_payments` |
| **Support** | `holidays`, `announcements`, `leave_applications`, `user_sessions` |
| **Views** | `v_attendance_summary`, `v_student_gpa`, `v_fee_outstanding` |

### MongoDB Collections

```bash
# Collections auto-created by Spring Data MongoDB
# study_materials, notifications, audit_logs, event_logs, chat_messages, report_snapshots
```

### Seed Test Data

```bash
psql -U postgres -d campus_connect -f tables/03_seed_data.sql
```

---

## Backend API Reference

### Core Controllers (14)
`AuthController`, `AdminController`, `AttendanceController`, `CourseController`, `DepartmentController`, `DevController`, `EventController`, `ExamController`, `MaterialController`, `NotificationController`, `OtpController`, `ProfileController`, `ResultController`, `RoleController`, `TenantController`, `TimetableController`

### PostgreSQL CRUD Controllers (21)

| Controller | Base Path | Key Endpoints |
|---|---|---|
| AcademicYear | `/api/academic-years` | CRUD, `GET /current`, `GET /label/{label}` |
| Program | `/api/programs` | CRUD, `GET /department/{id}` |
| Batch | `/api/batches` | CRUD, `GET /program/{id}` |
| ProgramCourse | `/api/program-courses` | CRUD, `GET /program/{id}`, `GET /course/{id}` |
| Student | `/api/students` | CRUD, `GET /batch/{id}`, `GET /status/{status}` |
| Faculty | `/api/faculty` | CRUD, `GET /department/{id}` |
| FacultyEducation | `/api/faculty-education` | CRUD, `GET /faculty/{id}` |
| FacultyWorkExperience | `/api/faculty-work-experience` | CRUD, `GET /faculty/{id}` |
| CourseAssignment | `/api/course-assignments` | CRUD, `GET /faculty/{id}`, `GET /course/{id}` |
| AttendanceSession | `/api/attendance-sessions` | CRUD, `GET /course-assignment/{id}` |
| ExamSchedule | `/api/exam-schedules` | CRUD, `GET /exam/{id}`, `GET /room/{id}` |
| Mark | `/api/marks` | CRUD, `GET /student/{id}`, `GET /exam/{id}` |
| FeeStructure | `/api/fee-structures` | CRUD, `GET /program/{id}` |
| FeeInvoice | `/api/fee-invoices` | CRUD, `GET /student/{id}`, `GET /status/{status}` |
| FeePayment | `/api/fee-payments` | CRUD, `GET /student/{id}`, `GET /status/{status}` |
| Announcement | `/api/announcements` | CRUD, `GET /active`, `GET /tenant/{id}` |
| Holiday | `/api/holidays` | CRUD, `GET /tenant/{id}` |
| LeaveApplication | `/api/leave-applications` | CRUD, `GET /user/{id}`, `GET /status/{status}` |
| Permission | `/api/permissions` | CRUD, `GET /module/{m}/action/{a}` |
| RolePermission | `/api/role-permissions` | CRUD, `GET /role/{id}`, `GET /permission/{id}` |
| Room | `/api/rooms` | CRUD, `GET /room-number/{n}`, `GET /building/{b}`, `GET /type/{t}` |

### MongoDB Document Controllers (5)

| Controller | Base Path |
|---|---|
| AuditLogDocument | `/api/documents/audit-logs` |
| ChatMessageDocument | `/api/documents/chat-messages` |
| ReportSnapshotDocument | `/api/documents/report-snapshots` |
| MaterialDocument | `/api/documents/materials` |
| NotificationDocument | `/api/documents/notifications` |

### Canonical Controllers (3)

| Controller | Base Path |
|---|---|
| CourseCanonical | `/api/canonical/postgres/tenants/{tenantId}/courses` |
| DepartmentCanonical | `/api/canonical/postgres/tenants/{tenantId}/departments` |
| TenantCanonical | `/api/canonical/postgres/tenants` |

### API Standards
- **200** OK — successful GET/PUT
- **201** Created — successful POST
- **204** No Content — successful DELETE
- **400** Bad Request — validation errors
- **401** Unauthorized — invalid/missing JWT
- **404** Not Found — resource missing

---

## Frontend Services & Hooks

### 24 API Services

All services are in `frontend/src/lib/services/` and exported via `index.ts`.

```typescript
import { programService } from "@/lib/services";

await programService.getAll();
await programService.getById(id);
await programService.create(data);
await programService.update(id, data);
await programService.delete(id);
```

| Domain | Services |
|---|---|
| **Academic** | `academicYearService`, `programService`, `batchService`, `programCourseService` |
| **Users** | `studentService`, `facultyService`, `facultyEducationService`, `facultyWorkExperienceService` |
| **Academics** | `courseAssignmentService`, `attendanceSessionService`, `examScheduleService`, `markService` |
| **Finance** | `feeStructureService`, `feeInvoiceService`, `feePaymentService` |
| **Admin** | `announcementService`, `holidayService`, `leaveApplicationService`, `permissionService`, `rolePermissionService`, `roomService` |
| **Documents** | `auditLogDocumentService`, `chatMessageDocumentService`, `reportSnapshotDocumentService` |

### React Query Hooks

```typescript
import { usePrograms, useCreateProgram } from "@/hooks/useApi";

function ProgramsPage() {
  const { data: programs, isLoading } = usePrograms();
  const createProgram = useCreateProgram();

  if (isLoading) return <div>Loading...</div>;

  return (
    <div>
      {programs?.map(p => <div key={p.id}>{p.name}</div>)}
      <button onClick={() => createProgram.mutateAsync(newData)}>
        Create Program
      </button>
    </div>
  );
}
```

### Type Definitions

All types in `frontend/src/types/models.ts`:

```typescript
import type { Program, Student, Faculty, Batch, AcademicYear } from "@/types/models";
```

---

## User Roles & Routes

### Admin (9 pages)
`/admin/dashboard`, `/admin/departments`, `/admin/programs`, `/admin/courses`, `/admin/batches`, `/admin/students`, `/admin/faculty`, `/admin/events`, `/admin/timetable`

### Faculty (8 pages)
`/faculty/dashboard`, `/faculty/courses`, `/faculty/attendance`, `/faculty/exams`, `/faculty/results`, `/faculty/materials`, `/faculty/events`, `/faculty/timetable`

### Student (8 pages)
`/student/dashboard`, `/student/courses`, `/student/attendance`, `/student/exams`, `/student/results`, `/student/materials`, `/student/events`, `/student/timetable`

### Super Admin (1 page)
`/super-admin/dashboard`

### Auth (3 pages)
`/login`, `/register`, `/forgot-password`

### Other (2 pages)
`/notifications`, `/profile`

---

## Test Accounts

> ⚠️ **For development/testing only.** Change all credentials before production.

All accounts use password: `password123`

| Role | Email | Tenant |
|---|---|---|
| Super Admin | `superadmin@mitcoep.edu` | MIT College of Engineering |
| Admin + HOD | `admin.cs@mitcoep.edu` | MIT College of Engineering |
| Faculty | `prof.mehta@mitcoep.edu` | MIT College of Engineering |
| Faculty | `prof.verma@mitcoep.edu` | MIT College of Engineering |
| Student | `student.arjun@mitcoep.edu` | MIT College of Engineering |
| Student | `student.neha@mitcoep.edu` | MIT College of Engineering |
| Student | `student.karan@mitcoep.edu` | MIT College of Engineering |
| Student | `student.priya@mitcoep.edu` | MIT College of Engineering |
| Admin | `admin@gu.ac.in` | Gujarat University |

---

## Troubleshooting

| Problem | Solution |
|---|---|
| Login returns 400 | `mfaEnabled` was changed from `boolean` to `Boolean` — ensure entity uses wrapper type with default `false` |
| API returns 401 | Check JWT token in localStorage; token may be expired |
| Router conflicts | The legacy `/src/pages/` directory was removed — use only App Router (`/src/app/`) |
| Hook returns undefined | Check if the query is enabled and the component is wrapped with QueryClientProvider |
| TypeScript errors | Import types from `@/types/models` |
| DB NULL errors | Run `UPDATE users SET mfa_enabled = FALSE WHERE mfa_enabled IS NULL;` |

---

## Roadmap

### P0 — MVP (Must-have)
- [x] Tenant onboarding + Identity + RBAC
- [x] Academic masters (department/program/course/batch)
- [x] Student and faculty management
- [x] Timetable + attendance + marks
- [x] Notifications + announcements
- [x] Backend controllers (41) + Frontend services (24)

### P1 — High Value
- [x] Finance (fee structures, invoices, payments)
- [x] Leave workflows
- [ ] Reporting snapshots and operational dashboards
- [ ] Super-admin controls for full tenant lifecycle

### P2 — Scale & Depth
- [ ] Chat module hardening
- [ ] Advanced analytics and predictive alerts
- [ ] External integrations (LMS, payment gateways, SSO)
- [ ] Real-time updates (WebSocket)
- [ ] CI/CD pipeline + automated testing

---

*Last Updated: March 22, 2026*
