# CampusConnect — SaaS College Management System
## Project Planning Document

---

| Field | Details |
|-------|---------|
| **Document Version** | 1.0 |
| **Date** | March 8, 2026 |
| **Project** | CampusConnect (mono-campus-connect) |
| **Package** | `com.monocampusconnect` |
| **Status** | Active Development |

---

## Table of Contents

1. [Project Overview & Goals](#1-project-overview--goals)
2. [SaaS Architecture Plan](#2-saas-architecture-plan)
3. [Technology Stack](#3-technology-stack)
4. [Module Breakdown](#4-module-breakdown)
5. [Development Phases & Milestones](#5-development-phases--milestones)
6. [Database Schema Plan](#6-database-schema-plan)
7. [API Endpoint Plan](#7-api-endpoint-plan)
8. [Security Plan](#8-security-plan)
9. [File Storage Plan](#9-file-storage-plan)
10. [Testing Strategy](#10-testing-strategy)
11. [Deployment Strategy](#11-deployment-strategy)
12. [Timeline & Roadmap](#12-timeline--roadmap)
13. [Risks & Mitigations](#13-risks--mitigations)
14. [Definition of Done](#14-definition-of-done)

---

## 1. Project Overview & Goals

### 1.1 Vision
> *CampusConnect is a cloud-native SaaS platform that centralizes and simplifies college management for students, faculty, and administrators — enabling better organization, communication, and academic excellence.*

### 1.2 Business Goals

| Goal | Metric |
|------|--------|
| Digitize college management | 0 manual spreadsheets for core operations |
| Improve attendance tracking accuracy | 100% digital attendance records |
| Centralize study materials | All course materials in one place |
| Streamline result management | Instant result publication & access |
| Multi-tenant SaaS monetization | Onboard 10+ colleges in Year 1 |
| Reduce admin overhead | 50% reduction in manual data entry |

### 1.3 Current State
- ✅ Spring Boot monolith bootstrapped (`com.monocampusconnect`, Spring Boot 3.4.5, Java 21)
- ✅ PostgreSQL integration (`ccdb` database)
- ✅ JWT Authentication with Spring Security
- ✅ Core modules: Auth, Profile, Course, Exam, Material, Result, Event
- ✅ AWS S3 configured (`campusconnectst`, `ap-south-1`)
- ✅ Swagger UI enabled
- 🔲 Multi-tenancy NOT yet implemented
- 🔲 Attendance module NOT yet implemented
- 🔲 Timetable module NOT yet implemented
- 🔲 OTP/email verification NOT yet implemented

### 1.4 Target State (v1.0 Release)
A fully functional SaaS platform where multiple colleges can independently manage their academic operations with complete data isolation, role-based access, and a clean REST API consumed by a modern frontend.

---

## 2. SaaS Architecture Plan

### 2.1 Multi-Tenancy Strategy

**Decision: Shared Database + Shared Schema with `tenant_id` discriminator**

```
Why not schema-per-tenant?
  ✗ Requires dynamic schema switching
  ✗ Complex migrations across N schemas
  ✗ Harder to query at platform level

Why shared schema + tenant_id?
  ✓ Simple to implement on existing monolith
  ✓ Easy database migrations (one schema)
  ✓ Cost-effective at startup/scale
  ✓ Can migrate to schema-per-tenant later
```

### 2.2 Tenant Resolution Flow

```
HTTP Request
    │
    ▼
JwtAuthenticationFilter
    │  Extract JWT
    ▼
JWT Claims: { userId, tenantId, role }
    │
    ▼
TenantContextHolder.setTenantId(tenantId)  ← ThreadLocal
    │
    ▼
Service Layer → Repository
    │  All queries automatically scoped
    ▼
WHERE tenant_id = :currentTenantId
    │
    ▼
HTTP Response → TenantContextHolder.clear()
```

### 2.3 Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                    CampusConnect Platform                        │
│                                                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐  │
│  │  College ABC │  │  College XYZ │  │     College PQR      │  │
│  │  (Tenant 1)  │  │  (Tenant 2)  │  │     (Tenant 3)       │  │
│  └──────┬───────┘  └──────┬───────┘  └──────────┬───────────┘  │
│         │                  │                      │              │
│  ┌──────▼──────────────────▼──────────────────────▼──────────┐  │
│  │            Load Balancer / API Gateway (Nginx)             │  │
│  └──────────────────────────┬─────────────────────────────────┘  │
│                             │                                    │
│  ┌──────────────────────────▼─────────────────────────────────┐  │
│  │         Spring Boot Application (Port 8080)                 │  │
│  │                                                             │  │
│  │  ┌─────────────────┐  ┌────────────────┐  ┌────────────┐  │  │
│  │  │  Auth + OTP     │  │  Tenant Filter │  │  CORS      │  │  │
│  │  │  Controller     │  │  (JWT Resolve) │  │  Config    │  │  │
│  │  └─────────────────┘  └────────────────┘  └────────────┘  │  │
│  │                                                             │  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌─────────────┐  │  │
│  │  │  Course  │ │   Exam   │ │ Material │ │   Result    │  │  │
│  │  │ Service  │ │ Service  │ │ Service  │ │  Service    │  │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └─────────────┘  │  │
│  │                                                             │  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌─────────────┐  │  │
│  │  │Attendance│ │Timetable │ │  Event   │ │Notification │  │  │
│  │  │ Service  │ │ Service  │ │ Service  │ │  Service    │  │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └─────────────┘  │  │
│  └────────────────────────────┬────────────────────────────────┘  │
│                               │                                    │
│  ┌────────────────────────────▼────────────────────────────────┐  │
│  │                  Data Layer                                  │  │
│  │  ┌─────────────────────┐      ┌──────────────────────────┐  │  │
│  │  │  PostgreSQL (ccdb)  │      │  AWS S3 (campusconnectst)│  │  │
│  │  │  localhost:5432     │      │  ap-south-1              │  │  │
│  │  └─────────────────────┘      └──────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 3. Technology Stack

### 3.1 Backend

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 21 | Core language (LTS) |
| Spring Boot | 3.4.5 | Application framework |
| Spring Security | 6.x | Authentication & authorization |
| Spring Data JPA | 3.x | Database ORM |
| Spring Validation | 3.4.5 | Input validation |
| Spring Actuator | 3.4.5 | Health checks & metrics |
| Hibernate | 6.x | JPA implementation |
| jjwt (JJWT) | 0.12.6 | JWT generation & validation |
| Lombok | Latest | Boilerplate reduction |
| Springdoc OpenAPI | 2.2.0 | Swagger UI & API docs |
| Apache Commons IO | 2.14.0 | File I/O utilities |

### 3.2 Database & Storage

| Technology | Version | Purpose |
|-----------|---------|---------|
| PostgreSQL | 15+ | Primary relational database |
| AWS S3 SDK | 1.12.587 | File & media storage |
| HikariCP | Built-in | Connection pooling |
| Flyway *(planned)* | 9.x | Schema version migration |

### 3.3 Infrastructure & DevOps *(Planned)*

| Technology | Purpose |
|-----------|---------|
| Docker | Containerization |
| Docker Compose | Local dev multi-service setup |
| AWS EC2 / ECS | Application hosting |
| AWS RDS | Managed PostgreSQL in production |
| AWS S3 | File storage (already configured) |
| AWS SES | Transactional email (OTP, notifications) |
| GitHub Actions | CI/CD pipeline |
| Nginx | Reverse proxy / load balancer |

### 3.4 Frontend *(Separate Project)*

| Technology | Purpose |
|-----------|---------|
| React / Vue 3 | SPA framework |
| Vite | Build tool |
| Axios | HTTP client |
| React Router / Vue Router | Client-side routing |
| Tailwind CSS | Styling |
| Dev Ports | 5173, 5174, 5175 (already in CORS config) |

---

## 4. Module Breakdown

### 4.1 Module Status & Ownership

| Module | Package | Status | Phase |
|--------|---------|--------|-------|
| Authentication | `controller/AuthController` | ✅ Built | Phase 1 |
| JWT Config | `config/JwtConfig` | ✅ Built | Phase 1 |
| Security Config | `config/SecurityConfig` | ✅ Built | Phase 1 |
| User/Profile | `controller/ProfileController` | ✅ Built | Phase 1 |
| Course Mgmt | `controller/CourseController` | ✅ Built | Phase 2 |
| Exam Mgmt | `controller/ExamController` | ✅ Built | Phase 2 |
| Results | `controller/ResultController` | ✅ Built | Phase 2 |
| Study Materials | `controller/MaterialController` | ✅ Built | Phase 2 |
| Events | `controller/EventController` | ✅ Built | Phase 2 |
| Exception Handling | `exception/GlobalExceptionHandler` | ✅ Built | Phase 1 |
| OTP Module | `controller/OtpController` | 🔲 Planned | Phase 1 |
| Admin Management | `controller/AdminController` | 🔲 Planned | Phase 2 |
| Attendance | `controller/AttendanceController` | 🔲 Planned | Phase 3 |
| Timetable | `controller/TimetableController` | 🔲 Planned | Phase 3 |
| Notifications | `controller/NotificationController` | 🔲 Planned | Phase 4 |
| Department | `controller/DepartmentController` | 🔲 Planned | Phase 2 |
| Tenant Management | `controller/TenantController` | 🔲 Planned | Phase 3 |
| Super Admin | `controller/SuperAdminController` | 🔲 Planned | Phase 3 |

### 4.2 Model/Entity Ownership

| Entity | Table | Status |
|--------|-------|--------|
| `User` | `users` | ✅ Built |
| `Course` | `courses` | ✅ Built |
| `Exam` | `exams` | ✅ Built |
| `Result` | `results` | ✅ Built |
| `ResultDetail` | `result_details` | ✅ Built |
| `Material` | `materials` | ✅ Built |
| `Event` | `events` | ✅ Built |
| `Tenant` | `tenants` | 🔲 Planned |
| `Department` | `departments` | 🔲 Planned |
| `Attendance` | `attendance` | 🔲 Planned |
| `TimetableEntry` | `timetable_entries` | 🔲 Planned |
| `Notification` | `notifications` | 🔲 Planned |
| `OtpToken` | `otp_tokens` | 🔲 Planned |

---

## 5. Development Phases & Milestones

---

### ✅ Phase 1 — Foundation & Core Auth *(Completed)*

**Goal:** Establish a working Spring Boot application with authentication, security, and basic user management.

| Task | Status |
|------|--------|
| Spring Boot 3.4.5 project setup | ✅ Done |
| PostgreSQL datasource configuration | ✅ Done |
| User entity with roles (ADMIN, FACULTY, STUDENT) | ✅ Done |
| JWT configuration (jjwt 0.12.6) | ✅ Done |
| Spring Security + BCrypt password encoding | ✅ Done |
| AuthController (register/login) | ✅ Done |
| ProfileController (view/update profile) | ✅ Done |
| Global exception handler | ✅ Done |
| Input validators | ✅ Done |
| Swagger/OpenAPI setup | ✅ Done |
| AWS S3 configuration | ✅ Done |

---

### ✅ Phase 2 — Academic Modules *(Completed)*

**Goal:** Implement all core academic management modules.

| Task | Status |
|------|--------|
| Course model + CRUD API | ✅ Done |
| Exam model + scheduling API | ✅ Done |
| Exam types (MIDTERM, FINAL, QUIZ, ASSIGNMENT) | ✅ Done |
| Result model + entry API | ✅ Done |
| ResultDetail (per-question breakdown) | ✅ Done |
| Material model + upload/download API | ✅ Done |
| Event model + posting API | ✅ Done |
| Role-based endpoint protection | ✅ Done |

---

### 🔲 Phase 3 — SaaS Multi-Tenancy *(Next Priority)*

**Goal:** Transform the single-tenant app into a true multi-tenant SaaS platform.

**Estimated Duration:** 3–4 weeks

| Task | Priority | Notes |
|------|----------|-------|
| Create `Tenant` entity & `tenants` table | P1 | UUID primary key |
| Add `SUPER_ADMIN` role to `User.Role` enum | P1 | New role |
| Add `tenantId` column to ALL existing entities | P1 | Shared schema strategy |
| Create `TenantContextHolder` (ThreadLocal) | P1 | Per-request tenant context |
| Update `JwtConfig` to embed `tenantId` in claims | P1 | Token carries tenant |
| Create `TenantFilter` to resolve tenant from JWT | P1 | Runs before controllers |
| Update all repositories to filter by `tenantId` | P1 | Query-level isolation |
| Create `TenantController` (`/api/tenants/**`) | P1 | SUPER_ADMIN only |
| Create `SuperAdminController` | P1 | Platform management |
| Create `AdminController` (`/api/admin/**`) | P1 | Tenant-level admin |
| Create OTP module (entity, service, controller) | P1 | Email-based |
| Integrate Spring Mail / AWS SES | P1 | OTP delivery |
| Add `Department` entity & API | P2 | Tenant scoped |
| Update SecurityConfig for new roles/endpoints | P1 | SUPER_ADMIN paths |
| Add Flyway for schema migrations | P2 | Replace ddl-auto=update |

**Key Classes to Create:**

```java
// New files needed:
com.monocampusconnect.model.Tenant
com.monocampusconnect.model.Department
com.monocampusconnect.model.OtpToken
com.monocampusconnect.config.TenantContextHolder
com.monocampusconnect.config.TenantFilter
com.monocampusconnect.controller.TenantController
com.monocampusconnect.controller.SuperAdminController
com.monocampusconnect.controller.AdminController
com.monocampusconnect.controller.OtpController
com.monocampusconnect.controller.DepartmentController
com.monocampusconnect.service.TenantService
com.monocampusconnect.service.OtpService
com.monocampusconnect.service.EmailService
com.monocampusconnect.service.DepartmentService
com.monocampusconnect.repository.TenantRepository
com.monocampusconnect.repository.DepartmentRepository
com.monocampusconnect.repository.OtpTokenRepository
com.monocampusconnect.dto.TenantRequest
com.monocampusconnect.dto.TenantResponse
com.monocampusconnect.dto.OtpRequest
com.monocampusconnect.dto.DepartmentRequest
```

---

### 🔲 Phase 4 — Attendance & Timetable *(Weeks 5–7)*

**Goal:** Implement the two most requested missing features.

**Estimated Duration:** 2–3 weeks

| Task | Priority |
|------|----------|
| `Attendance` entity & table | P1 |
| `AttendanceController` (`/api/attendance/**`) | P1 |
| `AttendanceService` (mark, view, calculate %) | P1 |
| Attendance report by student/course | P2 |
| Attendance low-percentage alert | P2 |
| `TimetableEntry` entity & table | P1 |
| `TimetableController` (`/api/timetable/**`) | P1 |
| `TimetableService` (CRUD, view by role) | P1 |
| Timetable conflict validation | P2 |
| Attendance bulk mark (entire class) | P2 |

---

### 🔲 Phase 5 — Notifications & Analytics *(Weeks 8–10)*

**Goal:** Add proactive communication and basic reporting.

**Estimated Duration:** 2–3 weeks

| Task | Priority |
|------|----------|
| `Notification` entity & table | P2 |
| `NotificationController` (`/api/notifications/**`) | P2 |
| `NotificationService` (create, read, mark read) | P2 |
| Email notifications for results | P2 |
| Email notifications for exam schedule | P2 |
| Attendance alert emails | P2 |
| Admin dashboard stats API | P2 |
| Result statistics API (pass rate, avg) | P2 |
| Material stats API | P2 |
| Attendance % report API | P2 |

---

### 🔲 Phase 6 — Hardening & Production Readiness *(Weeks 11–12)*

**Goal:** Make the system production-ready, secure, and deployable.

**Estimated Duration:** 2 weeks

| Task | Priority |
|------|----------|
| Replace `ddl-auto=update` with Flyway migrations | P1 |
| Externalize all secrets to env variables | P1 |
| Fix hardcoded JWT secret in `application.properties` | P1 |
| Fix hardcoded AWS credentials | P1 |
| Add rate limiting (Bucket4j or similar) | P2 |
| Add API request logging (MDC with tenantId) | P2 |
| Write unit tests (service layer, ≥70% coverage) | P2 |
| Write integration tests (controller layer) | P2 |
| Dockerize application (`Dockerfile` + `docker-compose.yml`) | P1 |
| GitHub Actions CI/CD pipeline | P2 |
| HTTPS / SSL configuration | P1 |
| Production CORS configuration | P1 |
| Performance testing (k6 / JMeter) | P3 |
| Security audit (OWASP Top 10 checklist) | P2 |

---

## 6. Database Schema Plan

### 6.1 Multi-Tenancy Migration Strategy

**Step 1:** Create `tenants` table with UUID PK
**Step 2:** Add `tenant_id UUID NOT NULL` column to all existing tables
**Step 3:** Create foreign key constraints
**Step 4:** Add composite indexes: `(tenant_id, entity_specific_field)`
**Step 5:** Update all JPA entities with `@Column(name="tenant_id")`

### 6.2 Index Strategy

```sql
-- Performance indexes for tenant-scoped queries
CREATE INDEX idx_users_tenant_id ON users(tenant_id);
CREATE INDEX idx_users_tenant_email ON users(tenant_id, email);
CREATE INDEX idx_courses_tenant_id ON courses(tenant_id);
CREATE INDEX idx_exams_tenant_id ON exams(tenant_id);
CREATE INDEX idx_results_tenant_student ON results(tenant_id, student_id);
CREATE INDEX idx_attendance_tenant_student ON attendance(tenant_id, student_id);
CREATE INDEX idx_attendance_tenant_course ON attendance(tenant_id, course_code);
CREATE INDEX idx_materials_tenant_course ON materials(tenant_id, course_code);
CREATE INDEX idx_events_tenant_id ON events(tenant_id);
CREATE INDEX idx_notifications_tenant_user ON notifications(tenant_id, user_id);
```

### 6.3 Entity Relationship Diagram (Simplified)

```
tenants (1) ─────────── (N) users
tenants (1) ─────────── (N) courses
tenants (1) ─────────── (N) departments
tenants (1) ─────────── (N) exams
tenants (1) ─────────── (N) results
tenants (1) ─────────── (N) materials
tenants (1) ─────────── (N) events
tenants (1) ─────────── (N) attendance
tenants (1) ─────────── (N) timetable_entries
tenants (1) ─────────── (N) notifications

users (1) ──────────── (N) results       [student_id]
users (1) ──────────── (N) attendance    [student_id]
exams (1) ──────────── (N) results
exams (1) ──────────── (N) result_details
results (1) ─────────── (N) result_details
courses (1) ─────────── (N) materials
courses (1) ─────────── (N) attendance
courses (1) ─────────── (N) timetable_entries
```

### 6.4 Planned Flyway Migration Files

```
src/main/resources/db/migration/
├── V1__initial_schema.sql          (current tables)
├── V2__add_tenant_id.sql           (Phase 3)
├── V3__create_tenants_table.sql    (Phase 3)
├── V4__create_attendance_table.sql (Phase 4)
├── V5__create_timetable_table.sql  (Phase 4)
├── V6__create_notifications.sql    (Phase 5)
├── V7__add_departments.sql         (Phase 3)
└── V8__add_otp_tokens.sql          (Phase 3)
```

---

## 7. API Endpoint Plan

### 7.1 Authentication (`/api/auth`)

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | Public | Register new user |
| POST | `/api/auth/login` | Public | Login, returns JWT |
| POST | `/api/auth/refresh` | Authenticated | Refresh JWT token |
| POST | `/api/auth/logout` | Authenticated | Invalidate token |

### 7.2 OTP (`/api/otp`) — *Planned*

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | `/api/otp/send` | Public | Send OTP to email |
| POST | `/api/otp/verify` | Public | Verify OTP code |
| POST | `/api/otp/resend` | Public | Resend OTP |

### 7.3 Profile (`/api/profile`)

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/profile` | All | Get own profile |
| PUT | `/api/profile` | All | Update own profile |
| POST | `/api/profile/picture` | All | Upload profile picture |

### 7.4 Admin (`/api/admin`) — *Planned*

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/admin/users` | ADMIN | List all users in tenant |
| POST | `/api/admin/users` | ADMIN | Create user (any role) |
| PUT | `/api/admin/users/{id}` | ADMIN | Edit user |
| DELETE | `/api/admin/users/{id}` | ADMIN | Deactivate user |
| GET | `/api/admin/users/{id}` | ADMIN | Get user by ID |
| GET | `/api/admin/stats` | ADMIN | Dashboard statistics |

### 7.5 Courses (`/api/courses`)

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/courses` | All | List all courses |
| POST | `/api/courses` | ADMIN, FACULTY | Create course |
| GET | `/api/courses/{id}` | All | Get course by ID |
| PUT | `/api/courses/{id}` | ADMIN, FACULTY | Update course |
| DELETE | `/api/courses/{id}` | ADMIN | Delete course |

### 7.6 Exams (`/api/exams`)

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/exams` | FACULTY, ADMIN | List all exams |
| POST | `/api/exams` | FACULTY, ADMIN | Create exam |
| GET | `/api/exams/{id}` | All | Get exam by ID |
| PUT | `/api/exams/{id}` | FACULTY, ADMIN | Update exam |
| DELETE | `/api/exams/{id}` | FACULTY, ADMIN | Delete exam |
| GET | `/api/exams/type/{type}` | ADMIN | Filter by type |
| GET | `/api/exams/student/{studentId}` | STUDENT, FACULTY, ADMIN | Student's exams |

### 7.7 Materials (`/api/materials`)

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/materials` | FACULTY, ADMIN | List all materials |
| POST | `/api/materials` | FACULTY, ADMIN | Upload material |
| GET | `/api/materials/{id}` | All | Get/download material |
| PUT | `/api/materials/{id}` | FACULTY, ADMIN | Update material |
| DELETE | `/api/materials/{id}` | FACULTY, ADMIN | Delete material |
| GET | `/api/materials/course/{code}` | All | Materials by course |
| GET | `/api/materials/type/{type}` | ADMIN | Filter by type |

### 7.8 Results (`/api/results`)

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/results` | FACULTY, ADMIN | List all results |
| POST | `/api/results` | FACULTY, ADMIN | Add result |
| GET | `/api/results/{id}` | All | Get result |
| PUT | `/api/results/{id}` | FACULTY, ADMIN | Update result |
| DELETE | `/api/results/{id}` | FACULTY, ADMIN | Delete result |
| GET | `/api/results/student/{studentId}` | All | Student's results |
| GET | `/api/results/status/{status}` | ADMIN | Filter by status |

### 7.9 Events (`/api/events`)

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/events` | All | List all events |
| POST | `/api/events` | ADMIN, FACULTY | Create event |
| GET | `/api/events/{id}` | All | Get event |
| PUT | `/api/events/{id}` | ADMIN, FACULTY | Update event |
| DELETE | `/api/events/{id}` | ADMIN | Delete event |

### 7.10 Attendance (`/api/attendance`) — *Planned*

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | `/api/attendance/mark` | FACULTY | Mark attendance |
| POST | `/api/attendance/bulk` | FACULTY | Bulk mark for class |
| GET | `/api/attendance/student/{id}` | All | Student's attendance |
| GET | `/api/attendance/course/{code}` | FACULTY, ADMIN | Course attendance |
| GET | `/api/attendance/percentage/{studentId}` | All | Attendance % per course |
| GET | `/api/attendance/report` | ADMIN | Full report |

### 7.11 Timetable (`/api/timetable`) — *Planned*

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/timetable` | All | Get full timetable |
| POST | `/api/timetable` | ADMIN | Create entry |
| PUT | `/api/timetable/{id}` | ADMIN | Update entry |
| DELETE | `/api/timetable/{id}` | ADMIN | Delete entry |
| GET | `/api/timetable/faculty/{id}` | FACULTY, ADMIN | Faculty's timetable |
| GET | `/api/timetable/semester/{sem}` | All | Timetable by semester |

### 7.12 Departments (`/api/departments`) — *Planned*

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/departments` | All | List departments |
| POST | `/api/departments` | ADMIN | Create department |
| PUT | `/api/departments/{id}` | ADMIN | Update department |
| DELETE | `/api/departments/{id}` | ADMIN | Delete department |

### 7.13 Notifications (`/api/notifications`) — *Planned*

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/notifications` | All | Get own notifications |
| PUT | `/api/notifications/{id}/read` | All | Mark as read |
| PUT | `/api/notifications/read-all` | All | Mark all as read |
| DELETE | `/api/notifications/{id}` | All | Delete notification |

### 7.14 Tenant Management (`/api/tenants`) — *Planned*

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/tenants` | SUPER_ADMIN | List all tenants |
| POST | `/api/tenants` | SUPER_ADMIN | Onboard new college |
| GET | `/api/tenants/{id}` | SUPER_ADMIN | Get tenant details |
| PUT | `/api/tenants/{id}` | SUPER_ADMIN | Update tenant |
| PUT | `/api/tenants/{id}/enable` | SUPER_ADMIN | Enable tenant |
| PUT | `/api/tenants/{id}/disable` | SUPER_ADMIN | Disable tenant |

---

## 8. Security Plan

### 8.1 Authentication Architecture

```
Login Request
    │
    ▼
POST /api/auth/login {email, password}
    │
    ▼
AuthService.login()
    ├── Load user from DB by email
    ├── BCrypt.matches(rawPassword, hashedPassword)
    └── If valid: JwtUtil.generateToken(userId, tenantId, role)
            │
            ▼
        JWT Payload:
        {
          "sub": "user@college.edu",
          "userId": 42,
          "tenantId": "uuid-of-college",
          "role": "FACULTY",
          "iat": 1741420800,
          "exp": 1741507200
        }
```

### 8.2 Request Authorization Flow

```
Every Protected Request
    │
    ▼
JwtAuthenticationFilter (OncePerRequestFilter)
    ├── Extract "Bearer TOKEN" from Authorization header
    ├── Validate JWT signature & expiry
    ├── Set SecurityContext (username, authorities)
    └── Set TenantContextHolder(tenantId)
            │
            ▼
    SecurityConfig.filterChain()
    (role-based path matching)
            │
            ▼
    Controller → Service → Repository
    (Repository always filters: WHERE tenant_id = :tenantId)
```

### 8.3 Role Hierarchy

```
SUPER_ADMIN
    └── Full platform access (all tenants)

ADMIN (per tenant)
    └── Full tenant access
        ├── Manage users (faculty + students)
        ├── Manage courses, exams, results
        ├── View all reports
        └── College settings

FACULTY (per tenant)
    ├── Manage own courses
    ├── Upload materials
    ├── Mark attendance
    ├── Create exams & enter results
    └── View student details (own courses)

STUDENT (per tenant)
    ├── View own profile
    ├── View own attendance, results, timetable
    ├── Download course materials
    └── View events (read-only everything)
```

### 8.4 Security Hardening Checklist

| Item | Status | Priority |
|------|--------|----------|
| BCrypt password hashing | ✅ Done | P1 |
| JWT stateless auth | ✅ Done | P1 |
| Role-based endpoint protection | ✅ Done | P1 |
| CSRF disabled (stateless API) | ✅ Done | P1 |
| Input validation (Spring Validation) | ✅ Done | P1 |
| Global exception handler | ✅ Done | P1 |
| Externalize JWT secret (env var) | 🔲 Planned | P1 |
| Externalize AWS credentials | 🔲 Planned | P1 |
| Tenant isolation enforcement | 🔲 Planned | P1 |
| HTTPS in production | 🔲 Planned | P1 |
| Production CORS restriction | 🔲 Planned | P1 |
| Rate limiting on auth endpoints | 🔲 Planned | P2 |
| OTP expiry (10 min) | 🔲 Planned | P1 |
| Audit logging | 🔲 Planned | P3 |
| OWASP Top 10 review | 🔲 Planned | P2 |

---

## 9. File Storage Plan

### 9.1 Current State
- **Development:** `@Lob byte[]` in database (User.profilePicture, Event.imageContent, Material.fileContent)
- **Production target:** AWS S3 (`campusconnectst` bucket, `ap-south-1` region)

### 9.2 S3 Key Structure

```
s3://campusconnectst/
├── {tenantId}/
│   ├── profiles/
│   │   └── {userId}/profile.{ext}
│   ├── materials/
│   │   └── {courseCode}/
│   │       └── {materialCode}/{filename}
│   └── events/
│       └── {eventId}/banner.{ext}
```

### 9.3 Migration Plan (Dev → Prod)
1. Add S3 key column to entities (e.g., `profilePictureKey`, `fileKey`, `imageKey`)
2. On upload: store file in S3, save S3 key in DB
3. On download: generate pre-signed S3 URL (valid 1 hour)
4. Deprecate `@Lob byte[]` fields after migration

---

## 10. Testing Strategy

### 10.1 Testing Pyramid

```
        ┌─────────┐
        │   E2E   │  ← Postman collections / integration flows
        ├─────────┤
        │  Integ  │  ← Spring Boot Test + TestContainers (PostgreSQL)
        ├─────────┤
        │  Unit   │  ← JUnit 5 + Mockito (Services, Validators)
        └─────────┘
```

### 10.2 Test Coverage Targets

| Layer | Tool | Target Coverage |
|-------|------|----------------|
| Service Layer | JUnit 5 + Mockito | ≥ 80% |
| Controller Layer | Spring MVC Test | ≥ 70% |
| Repository Layer | Spring Data Test + H2 | ≥ 60% |
| Overall | JaCoCo | ≥ 70% |

### 10.3 Key Test Scenarios

| Scenario | Priority |
|----------|----------|
| Login with valid credentials → JWT returned | P1 |
| Login with wrong password → 401 | P1 |
| Access protected endpoint without token → 401 | P1 |
| Student accesses admin endpoint → 403 | P1 |
| Tenant A cannot read Tenant B's data | P1 |
| Create course → verify in DB | P1 |
| Upload material → verify S3 key saved | P2 |
| Mark attendance → verify % calculation | P1 |
| OTP expiry → verify rejection after 10min | P2 |

---

## 11. Deployment Strategy

### 11.1 Environment Strategy

| Environment | Config | Database | S3 |
|------------|--------|----------|----|
| Development | `application.properties` | Local PostgreSQL | Local @Lob |
| Staging | `application-staging.properties` | AWS RDS PostgreSQL | S3 (staging bucket) |
| Production | Environment Variables | AWS RDS PostgreSQL | S3 (prod bucket) |

### 11.2 Docker Setup *(Planned)*

**`Dockerfile`:**
```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/mono-campus-connect-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**`docker-compose.yml`** (local dev):
```yaml
services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/ccdb
      - JWT_SECRET=${JWT_SECRET}
      - AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
      - AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}

  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: ccdb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

volumes:
  postgres_data:
```

### 11.3 CI/CD Pipeline *(Planned — GitHub Actions)*

```
┌─────────────────────────────────────────────────────────┐
│                   GitHub Actions Workflow                 │
│                                                          │
│  Push to main/develop                                    │
│         │                                                │
│         ▼                                                │
│  1. Checkout code                                        │
│  2. Set up Java 21                                       │
│  3. Run unit tests (mvn test)                            │
│  4. Run integration tests                                │
│  5. Build JAR (mvn package)                              │
│  6. Build Docker image                                   │
│  7. Push to ECR (AWS Container Registry)                 │
│  8. Deploy to ECS (production) or EC2 (staging)         │
└─────────────────────────────────────────────────────────┘
```

### 11.4 Production Infrastructure *(Target)*

```
Route 53 (DNS)
    │
    ▼
CloudFront (CDN)
    │
    ▼
Application Load Balancer
    │
    ├── ECS Service (Spring Boot app containers)
    │       │
    │       ▼
    │   AWS RDS PostgreSQL (Multi-AZ)
    │
    └── S3 (campusconnectst) ← Static files & media
```

---

## 12. Timeline & Roadmap

### 12.1 Development Timeline

| Phase | Description | Duration | Start | End |
|-------|-------------|----------|-------|-----|
| **Phase 1** | Foundation & Auth | ✅ Completed | - | Mar 2026 |
| **Phase 2** | Academic Modules | ✅ Completed | - | Mar 2026 |
| **Phase 3** | SaaS Multi-Tenancy + OTP + Admin | 3–4 weeks | Mar 8, 2026 | Apr 5, 2026 |
| **Phase 4** | Attendance & Timetable | 2–3 weeks | Apr 6, 2026 | Apr 26, 2026 |
| **Phase 5** | Notifications & Analytics | 2–3 weeks | Apr 27, 2026 | May 17, 2026 |
| **Phase 6** | Hardening & Production Ready | 2 weeks | May 18, 2026 | Jun 1, 2026 |
| **v1.0 Release** | Production deployment | - | Jun 1, 2026 | Jun 1, 2026 |

### 12.2 Feature Roadmap (Gantt-Style)

```
Feature                          Mar    Apr    May    Jun
─────────────────────────────────────────────────────────
Phase 1 (Auth + Core)            ████
Phase 2 (Academic Modules)       ████
Phase 3 (Multi-Tenancy + OTP)    ████   ████
Phase 3 (Admin API + Dept)              ████
Phase 4 (Attendance)                    ████
Phase 4 (Timetable)                     ████   ██
Phase 5 (Notifications)                        ████
Phase 5 (Analytics/Reports)                    ████
Phase 6 (Docker + CI/CD)                       ████
Phase 6 (Testing + Security)                   ████
v1.0 Release 🚀                                      ██
```

### 12.3 Immediate Next Steps (This Week)

```
Priority Order for Phase 3:

1. [ ] Add SUPER_ADMIN to User.Role enum
2. [ ] Create Tenant entity + TenantRepository
3. [ ] Add tenant_id column to User entity (start with User, then others)
4. [ ] Create TenantContextHolder (ThreadLocal)
5. [ ] Update JwtConfig to include tenantId in token claims
6. [ ] Create TenantFilter (extracts tenantId from JWT into context)
7. [ ] Create OtpToken entity + OtpService + OtpController
8. [ ] Add Spring Mail dependency to pom.xml
9. [ ] Create AdminController (/api/admin/**)
10. [ ] Update SecurityConfig for new roles and endpoints
```

---

## 13. Risks & Mitigations

| Risk | Probability | Impact | Mitigation |
|------|------------|--------|------------|
| Multi-tenancy data leak (wrong tenant_id) | Medium | Critical | Automated tests for cross-tenant access; TenantContextHolder strictly enforced |
| JWT secret compromised | Low | Critical | Externalize to env vars; rotate regularly |
| S3 bucket public access | Medium | High | Bucket policy: private; use pre-signed URLs only |
| Database performance with high tenant count | Medium | High | Tenant-indexed queries; connection pooling |
| `ddl-auto=update` data loss in prod | High | Critical | Migrate to Flyway before any production deployment |
| OTP email delivery failure | Low | Medium | Retry mechanism; fallback SMS |
| File upload size limits | Low | Medium | Configure `spring.servlet.multipart.max-file-size=50MB` |
| CORS misconfiguration in prod | Medium | Medium | Restrict allowed origins to known domains |
| Hardcoded credentials in `application.properties` | High | Critical | Move to env vars immediately (Phase 3 task #1) |

---

## 14. Definition of Done

A feature is considered **Done** when:

- [ ] Code is written and follows project package structure
- [ ] All unit tests pass (`mvn test`)
- [ ] Test coverage ≥ 70% for the new service/controller
- [ ] Swagger/OpenAPI annotations added to all new endpoints
- [ ] Input validation added (Jakarta Validation annotations)
- [ ] Role-based access control applied in SecurityConfig
- [ ] Tenant isolation enforced (tenant_id filter in repository)
- [ ] Global exception handler covers new exception cases
- [ ] `application.properties` updated if new config needed
- [ ] No hardcoded secrets in code
- [ ] Code reviewed (if team project)
- [ ] Feature tested end-to-end via Postman or Swagger UI
- [ ] PLANNING.md updated to mark task as complete

---

*Document maintained by: CampusConnect Development Team*
*Last Updated: March 8, 2026*

