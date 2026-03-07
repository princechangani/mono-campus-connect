# Software Requirements Specification (SRS)
## CampusConnect — SaaS College Management System

---

| Field            | Details                                     |
|------------------|---------------------------------------------|
| **Document Version** | 1.0                                    |
| **Date**         | March 8, 2026                               |
| **Project**      | CampusConnect (mono-campus-connect)         |
| **Package**      | `com.monocampusconnect`                     |
| **Status**       | Draft                                       |

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Overall Description](#2-overall-description)
3. [System Architecture Overview](#3-system-architecture-overview)
4. [User Roles & Personas](#4-user-roles--personas)
5. [Functional Requirements](#5-functional-requirements)
   - 5.1 Authentication & OTP Module
   - 5.2 Super Admin Module
   - 5.3 College Admin Module
   - 5.4 Faculty Module
   - 5.5 Student Module
   - 5.6 Course Management Module
   - 5.7 Exam & Assessment Module
   - 5.8 Results & Grades Module
   - 5.9 Study Materials Module
   - 5.10 Attendance Module *(planned)*
   - 5.11 Timetable Module *(planned)*
   - 5.12 Events & Announcements Module
   - 5.13 Notifications Module *(planned)*
   - 5.14 SaaS Tenant Management Module
6. [Non-Functional Requirements](#6-non-functional-requirements)
7. [Database Requirements](#7-database-requirements)
8. [API Requirements Overview](#8-api-requirements-overview)
9. [SaaS Multi-Tenancy Requirements](#9-saas-multi-tenancy-requirements)
10. [Gap Analysis](#10-gap-analysis)
11. [Assumptions & Dependencies](#11-assumptions--dependencies)
12. [Appendix](#12-appendix)

---

## 1. Introduction

### 1.1 Purpose
This Software Requirements Specification (SRS) document describes the functional and non-functional requirements for **CampusConnect**, a SaaS (Software as a Service) College Management System. It serves as a blueprint for all development, testing, and deployment activities.

### 1.2 Scope
CampusConnect is a cloud-based, multi-tenant SaaS platform designed to streamline and automate academic and administrative operations across multiple colleges. The system provides dedicated interfaces and workflows for four primary user roles: **Super Admin**, **College Admin**, **Faculty**, and **Student**.

**In Scope:**
- User authentication (JWT + OTP)
- Role-based access control (RBAC)
- Course, exam, result, and material management
- Attendance and timetable tracking
- Event and announcement publishing
- Multi-tenant college onboarding
- File storage via AWS S3

**Out of Scope (v1.0):**
- Payment gateway / fee management
- Mobile native apps (iOS/Android)
- Live video classes

### 1.3 Definitions, Acronyms & Abbreviations

| Term | Definition |
|------|-----------|
| SaaS | Software as a Service |
| JWT | JSON Web Token |
| OTP | One-Time Password |
| RBAC | Role-Based Access Control |
| Tenant | An individual college/institution using the platform |
| Admin | College-level administrator |
| Super Admin | Platform-level administrator (CampusConnect operator) |
| CRUD | Create, Read, Update, Delete |
| AWS S3 | Amazon Web Services Simple Storage Service |
| API | Application Programming Interface |
| REST | Representational State Transfer |
| JPA | Java Persistence API |
| DTO | Data Transfer Object |

### 1.4 Overview
Section 2 provides a high-level system description. Sections 3–5 cover architecture and detailed requirements. Sections 6–8 cover non-functional requirements, database design, and API design. Section 9 covers SaaS-specific requirements. Section 10 provides a gap analysis of what is built vs. what is planned.

---

## 2. Overall Description

### 2.1 Product Perspective
CampusConnect is a standalone, cloud-hosted web application. It acts as a centralized platform that replaces fragmented, manual, or spreadsheet-based college management processes. Multiple colleges (tenants) subscribe to the platform and manage their operations independently, with full data isolation.

```
┌─────────────────────────────────────────────────────────────┐
│                   CampusConnect SaaS Platform               │
│                                                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │  College A   │  │  College B   │  │    College C     │  │
│  │  (Tenant 1)  │  │  (Tenant 2)  │  │   (Tenant 3)     │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
│                                                             │
│  ┌────────────────────────────────────────────────────────┐ │
│  │              Shared Application Layer                   │ │
│  │     Spring Boot 3.4.5 | Java 21 | REST APIs            │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                             │
│  ┌────────────────────┐  ┌─────────────────────────────┐   │
│  │  PostgreSQL (ccdb) │  │   AWS S3 (campusconnectst)  │   │
│  └────────────────────┘  └─────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Product Functions (Summary)

| Module | Core Functions |
|--------|---------------|
| Authentication | Register, Login, JWT tokens, OTP verification |
| User Management | CRUD users, role assignment, profile management |
| Course Management | Create, manage, and assign courses |
| Exam Management | Schedule exams, manage question banks |
| Results & Grades | Record, compute, and publish results |
| Study Materials | Upload/download PDFs, videos, documents |
| Attendance | Mark, view, and report attendance |
| Timetable | Create and view schedules |
| Events | Create and broadcast college events |
| Notifications | In-app and email notifications |
| Tenant Management | Onboard colleges, manage subscriptions |

### 2.3 User Classes and Characteristics

| Role | Description | Technical Level |
|------|-------------|----------------|
| Super Admin | CampusConnect operator, manages all tenants | High |
| College Admin | Institution administrator, manages all college data | Medium |
| Faculty | Teaching staff, manages courses and students | Medium |
| Student | Enrolled learners, view-only for most modules | Low |

### 2.4 Operating Environment
- **Backend:** Java 21, Spring Boot 3.4.5
- **Database:** PostgreSQL 15+
- **File Storage:** AWS S3 (region: `ap-south-1`, bucket: `campusconnectst`)
- **Authentication:** JWT (jjwt 0.12.6) + BCrypt password hashing
- **API Documentation:** Springdoc OpenAPI (Swagger UI at `/swagger-ui.html`)
- **Server Port:** 8080
- **Client:** Any modern web browser (Chrome, Firefox, Edge, Safari)

### 2.5 Design and Implementation Constraints
- Single deployable JAR (Spring Boot monolith) — SaaS multi-tenancy via shared schema + `tenant_id`
- Stateless REST API (JWT-based, no server sessions)
- CORS: Currently configured for `localhost:5173/5174/5175` (frontend dev servers)
- File storage delegated to AWS S3 for production; `@Lob byte[]` used in development

### 2.6 Assumptions and Dependencies
- Colleges subscribe and are onboarded by Super Admin
- Each user belongs to exactly one tenant (college)
- Frontend is a separate SPA (React/Vue/Angular) consuming the REST API
- Email service (SMTP/SES) will be configured for OTP delivery

---

## 3. System Architecture Overview

### 3.1 Layered Architecture

```
┌─────────────────────────────────────────────────────┐
│                  Presentation Layer                  │
│           (React SPA / Mobile Browser)               │
└─────────────────────────┬───────────────────────────┘
                          │ HTTPS / REST JSON
┌─────────────────────────▼───────────────────────────┐
│                  API Gateway / Load Balancer          │
└─────────────────────────┬───────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────┐
│              Application Layer (Spring Boot)          │
│  ┌──────────────┐  ┌────────────┐  ┌─────────────┐  │
│  │  Controllers │  │  Services  │  │  Validators │  │
│  └──────────────┘  └────────────┘  └─────────────┘  │
│  ┌──────────────┐  ┌────────────┐  ┌─────────────┐  │
│  │     DTOs     │  │   Models   │  │  Exception  │  │
│  └──────────────┘  └────────────┘  │   Handler   │  │
│                                    └─────────────┘  │
│  ┌─────────────────────────────────────────────────┐ │
│  │         Security Layer (JWT + Spring Security)   │ │
│  └─────────────────────────────────────────────────┘ │
└─────────────────────────┬───────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────┐
│                 Data Access Layer                     │
│  ┌──────────────────────┐  ┌────────────────────┐   │
│  │  JPA Repositories    │  │   AWS S3 Client    │   │
│  └──────────────────────┘  └────────────────────┘   │
└─────────────────────────┬───────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────┐
│                  Infrastructure Layer                 │
│  ┌───────────────┐  ┌──────────────────────────┐    │
│  │  PostgreSQL   │  │  AWS S3 (ap-south-1)     │    │
│  │  (ccdb)       │  │  (campusconnectst)        │    │
│  └───────────────┘  └──────────────────────────┘    │
└─────────────────────────────────────────────────────┘
```

### 3.2 Package Structure (com.monocampusconnect)

| Package | Responsibility |
|---------|---------------|
| `config` | Security, JWT, CORS, AWS configuration |
| `controller` | REST endpoint handlers |
| `dto` | Request/Response transfer objects |
| `exception` | Global exception handling, custom exceptions |
| `model` | JPA entities (database tables) |
| `repository` | Spring Data JPA repository interfaces |
| `service` | Business logic layer |
| `validator` | Input validation logic |

---

## 4. User Roles & Personas

### 4.1 Super Admin (Platform Level)
- **Persona:** CampusConnect operations team member
- **Responsibilities:** Onboard colleges, manage subscriptions, monitor platform health
- **Access:** Full platform-wide access
- **Enum Value (to add):** `SUPER_ADMIN`

### 4.2 College Admin (Tenant Level)
- **Persona:** Registrar, Principal, or IT Admin of the college
- **Responsibilities:** Add/remove faculty and students, manage academic structure
- **Access:** Full access within their tenant
- **Enum Value:** `ADMIN`

### 4.3 Faculty
- **Persona:** Professor or lecturer
- **Responsibilities:** Manage courses, upload materials, record attendance, schedule exams, post results
- **Access:** Read/write for academic modules; read for student details
- **Enum Value:** `FACULTY`

### 4.4 Student
- **Persona:** Enrolled college student
- **Responsibilities:** View own profile, attendance, timetable, marks, materials, events
- **Access:** Mostly read-only; own-profile write
- **Enum Value:** `STUDENT`

---

## 5. Functional Requirements

### 5.1 Authentication & OTP Module

| ID | Requirement | Priority |
|----|------------|----------|
| AUTH-01 | User shall be able to register with email, password, first/last name, role | P1 |
| AUTH-02 | User shall log in with email + password and receive a JWT token | P1 |
| AUTH-03 | JWT token shall expire after configured duration (default: 86400000ms / 24h) | P1 |
| AUTH-04 | Passwords shall be stored as BCrypt hashes | P1 |
| AUTH-05 | System shall expose `/api/otp/**` for OTP-based verification (email OTP) | P1 |
| AUTH-06 | OTP shall expire in 10 minutes | P2 |
| AUTH-07 | Password reset shall be OTP-based | P2 |
| AUTH-08 | Tenant ID shall be embedded in JWT claims | P1 |
| AUTH-09 | JWT filter shall extract and validate tenant context on every request | P1 |

### 5.2 Super Admin Module

| ID | Requirement | Priority |
|----|------------|----------|
| SA-01 | Super Admin shall be able to create and onboard new college tenants | P1 |
| SA-02 | Super Admin shall view all tenants and their usage statistics | P1 |
| SA-03 | Super Admin shall enable/disable tenant accounts | P1 |
| SA-04 | Super Admin shall manage subscription plans and billing status | P2 |
| SA-05 | Super Admin shall have read access to all tenant data for audit purposes | P2 |
| SA-06 | Super Admin dashboard shall show platform-level metrics | P2 |
| SA-07 | Super Admin shall not be able to modify tenant's academic data | P3 |

### 5.3 College Admin Module

| ID | Requirement | Priority |
|----|------------|----------|
| ADM-01 | Admin shall add new faculty users (with role FACULTY) | P1 |
| ADM-02 | Admin shall add new student users (with role STUDENT) | P1 |
| ADM-03 | Admin shall edit and deactivate user accounts | P1 |
| ADM-04 | Admin shall manage departments | P1 |
| ADM-05 | Admin shall view all courses in the college | P1 |
| ADM-06 | Admin shall create and manage academic semesters | P2 |
| ADM-07 | Admin shall generate and export reports (attendance, results) | P2 |
| ADM-08 | Admin shall manage college-wide events and announcements | P1 |
| ADM-09 | Admin shall configure college settings (name, logo, timezone) | P2 |
| ADM-10 | Admin shall view platform usage stats for their tenant | P3 |

### 5.4 Faculty Module

| ID | Requirement | Priority |
|----|------------|----------|
| FAC-01 | Faculty shall view and edit their own profile | P1 |
| FAC-02 | Faculty shall view courses assigned to them | P1 |
| FAC-03 | Faculty shall manage study materials for their courses | P1 |
| FAC-04 | Faculty shall create and schedule exams (MIDTERM, FINAL, QUIZ, ASSIGNMENT) | P1 |
| FAC-05 | Faculty shall enter and publish student results | P1 |
| FAC-06 | Faculty shall mark attendance for students per class | P1 |
| FAC-07 | Faculty shall view the timetable | P1 |
| FAC-08 | Faculty shall view student profiles in their courses | P2 |
| FAC-09 | Faculty shall post announcements for their courses | P2 |
| FAC-10 | Faculty shall upload materials to AWS S3 | P1 |

### 5.5 Student Module

| ID | Requirement | Priority |
|----|------------|----------|
| STU-01 | Student shall view and edit their own profile (name, phone, address, profile picture) | P1 |
| STU-02 | Student shall view their enrolled courses | P1 |
| STU-03 | Student shall view their attendance record | P1 |
| STU-04 | Student shall view their timetable | P1 |
| STU-05 | Student shall view their exam schedule | P1 |
| STU-06 | Student shall view their results and grades | P1 |
| STU-07 | Student shall download study materials | P1 |
| STU-08 | Student shall view college events and announcements | P1 |
| STU-09 | Student shall view notifications | P2 |
| STU-10 | Student shall NOT modify any academic records | P1 |

### 5.6 Course Management Module

| ID | Requirement | Priority |
|----|------------|----------|
| CRS-01 | Admin/Faculty shall create a course with code, name, department, credits, instructor, semester | P1 |
| CRS-02 | Admin shall assign faculty to courses | P1 |
| CRS-03 | Admin shall enroll students in courses | P1 |
| CRS-04 | All authenticated users shall view available courses | P1 |
| CRS-05 | Course code shall be unique per tenant | P1 |
| CRS-06 | Admin shall deactivate/archive courses | P2 |

**Existing Entity:** `Course` (id, courseCode, courseName, department, credits, instructor, semester)

### 5.7 Exam & Assessment Module

| ID | Requirement | Priority |
|----|------------|----------|
| EXM-01 | Faculty shall create an exam with code, title, description, dates, type | P1 |
| EXM-02 | Exam types supported: MIDTERM, FINAL, QUIZ, ASSIGNMENT | P1 |
| EXM-03 | Faculty shall enroll students in an exam | P1 |
| EXM-04 | Admin shall filter exams by type | P1 |
| EXM-05 | Faculty shall manage exam questions (ExamQuestionDTO) | P2 |
| EXM-06 | System shall track exam start/end dates | P1 |
| EXM-07 | Students shall view exams they are enrolled in | P1 |

**Existing Entity:** `Exam` (id, examCode, courseCode, title, description, startDate, endDate, type, enrolledStudents)

### 5.8 Results & Grades Module

| ID | Requirement | Priority |
|----|------------|----------|
| RES-01 | Faculty shall enter results for students (totalMarks, obtainedMarks, grade, status) | P1 |
| RES-02 | System shall compute grade automatically based on percentage | P2 |
| RES-03 | Admin shall filter results by status | P1 |
| RES-04 | Students shall view only their own results | P1 |
| RES-05 | System shall generate result statistics (pass rate, average, min, max) | P2 |
| RES-06 | Result details (per-question breakdown) shall be stored via ResultDetail | P2 |
| RES-07 | Faculty shall add comments to results | P2 |

**Existing Entities:** `Result` (id, studentId, examCode, courseCode, exam, resultDetails, totalMarks, obtainedMarks, grade, status, comments, resultDate), `ResultDetail`

### 5.9 Study Materials Module

| ID | Requirement | Priority |
|----|------------|----------|
| MAT-01 | Faculty shall upload study materials (PDF, DOC, PPT, VIDEO, LINK) | P1 |
| MAT-02 | Materials shall be stored in AWS S3 in production | P1 |
| MAT-03 | Students shall download/view materials for their courses | P1 |
| MAT-04 | Admin shall filter materials by type | P1 |
| MAT-05 | System shall track download count per material | P2 |
| MAT-06 | Faculty shall update or delete materials | P2 |
| MAT-07 | Material stats shall be available (total uploads, downloads) | P2 |

**Existing Entity:** `Material` (id, materialCode, courseCode, title, description, type, fileContent, uploadedBy, uploadedDate, fileSize, downloadCount, lastUpdatedDate, fileType)

### 5.10 Attendance Module *(Planned — Not Yet Implemented)*

| ID | Requirement | Priority |
|----|------------|----------|
| ATT-01 | Faculty shall mark attendance (Present/Absent/Late) for each class session | P1 |
| ATT-02 | System shall calculate attendance percentage per student per course | P1 |
| ATT-03 | Students shall view their own attendance record | P1 |
| ATT-04 | Admin shall generate attendance reports | P2 |
| ATT-05 | System shall alert students with attendance below threshold (e.g., <75%) | P2 |
| ATT-06 | Faculty shall view attendance summary per course | P1 |

**Planned Entity:** `Attendance` (id, tenantId, studentId, courseCode, date, status, markedBy, createdAt)

### 5.11 Timetable Module *(Planned — Not Yet Implemented)*

| ID | Requirement | Priority |
|----|------------|----------|
| TTB-01 | Admin shall create and publish weekly timetable | P1 |
| TTB-02 | Faculty shall view their teaching timetable | P1 |
| TTB-03 | Students shall view their class timetable | P1 |
| TTB-04 | Timetable shall include day, time slot, subject, faculty, room number | P1 |
| TTB-05 | Admin shall update/delete timetable entries | P2 |

**Planned Entity:** `TimetableEntry` (id, tenantId, day, timeSlot, courseCode, facultyId, roomNumber, semester, createdAt)

### 5.12 Events & Announcements Module

| ID | Requirement | Priority |
|----|------------|----------|
| EVT-01 | Admin/Faculty shall create events with title, description, image | P1 |
| EVT-02 | All users shall view published events | P1 |
| EVT-03 | Event images shall be stored in AWS S3 | P1 |
| EVT-04 | Admin shall delete/update events | P1 |
| EVT-05 | Events shall be scoped per tenant | P1 |

**Existing Entity:** `Event` (id, title, description, imageContent, postedBy, createdAt, updatedAt)

### 5.13 Notifications Module *(Planned — Not Yet Implemented)*

| ID | Requirement | Priority |
|----|------------|----------|
| NOT-01 | System shall send in-app notifications for exam schedules | P2 |
| NOT-02 | System shall send email notifications for result publication | P2 |
| NOT-03 | System shall send attendance alerts | P2 |
| NOT-04 | Admin shall broadcast announcements to all users | P2 |
| NOT-05 | Users shall mark notifications as read | P3 |

### 5.14 SaaS Tenant Management Module

| ID | Requirement | Priority |
|----|------------|----------|
| TNT-01 | Super Admin shall create a new tenant (college) | P1 |
| TNT-02 | Each tenant shall have a unique tenantId (UUID) | P1 |
| TNT-03 | All data entities shall be scoped by tenantId | P1 |
| TNT-04 | Tenant data shall be completely isolated from other tenants | P1 |
| TNT-05 | Super Admin shall enable/disable tenants | P1 |
| TNT-06 | Each tenant shall have configurable settings (name, logo, timezone) | P2 |
| TNT-07 | System shall support subscription plan management per tenant | P3 |

---

## 6. Non-Functional Requirements

### 6.1 Performance

| ID | Requirement | Target |
|----|------------|--------|
| NFR-P01 | API response time for standard queries | < 500ms (95th percentile) |
| NFR-P02 | File upload (materials/events) | < 10s for files up to 50MB |
| NFR-P03 | Concurrent users per tenant | ≥ 500 |
| NFR-P04 | Platform total concurrent users | ≥ 5,000 |
| NFR-P05 | Database query optimization (indexed tenant_id) | Mandatory |

### 6.2 Security

| ID | Requirement |
|----|------------|
| NFR-S01 | All endpoints (except public) must require valid JWT |
| NFR-S02 | Passwords must be BCrypt-hashed (never stored in plain text) |
| NFR-S03 | JWT secret must be externalized (env var), not hardcoded |
| NFR-S04 | Tenant isolation: user cannot access another tenant's data |
| NFR-S05 | HTTPS mandatory in production |
| NFR-S06 | AWS credentials must be via IAM roles/env vars, not hardcoded |
| NFR-S07 | CORS must be restricted to known origins in production |
| NFR-S08 | Input validation on all request bodies (Spring Validation) |
| NFR-S09 | SQL injection prevention via JPA parameterized queries |
| NFR-S10 | Rate limiting on auth endpoints (`/api/auth/**`, `/api/otp/**`) |

### 6.3 Scalability

| ID | Requirement |
|----|------------|
| NFR-SC01 | Architecture shall support horizontal scaling (stateless services) |
| NFR-SC02 | Database connection pooling (HikariCP — Spring Boot default) |
| NFR-SC03 | File storage on AWS S3 (unlimited scale) |
| NFR-SC04 | Application shall be containerizable (Docker-ready) |

### 6.4 Availability & Reliability

| ID | Requirement |
|----|------------|
| NFR-A01 | System uptime target: 99.9% (≤ 8.7 hours downtime/year) |
| NFR-A02 | Graceful error handling — no raw stack traces to client |
| NFR-A03 | Global exception handling via `GlobalExceptionHandler` |
| NFR-A04 | Database backups: daily automated backups |
| NFR-A05 | Health check endpoint via Spring Actuator |

### 6.5 Usability

| ID | Requirement |
|----|------------|
| NFR-U01 | API documentation available via Swagger UI (`/swagger-ui.html`) |
| NFR-U02 | Consistent JSON response format across all endpoints |
| NFR-U03 | Meaningful error messages with HTTP status codes |
| NFR-U04 | Internationalization (i18n) support for future expansion |

### 6.6 Maintainability

| ID | Requirement |
|----|------------|
| NFR-M01 | Code must follow standard Java/Spring Boot project structure |
| NFR-M02 | Unit test coverage ≥ 70% |
| NFR-M03 | All APIs documented with OpenAPI 3.0 annotations |
| NFR-M04 | Centralized configuration via `application.properties` / env vars |

---

## 7. Database Requirements

### 7.1 Technology
- **RDBMS:** PostgreSQL 15+
- **ORM:** Spring Data JPA (Hibernate)
- **Schema Migration:** Flyway *(planned — currently using `ddl-auto=update`)*
- **Database Name:** `ccdb`

### 7.2 Existing Tables

#### `users`
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | Auto-generated |
| tenant_id | UUID FK | Tenant association *(to add)* |
| email | VARCHAR | Unique per tenant |
| password | VARCHAR | BCrypt hash |
| first_name | VARCHAR | |
| last_name | VARCHAR | |
| profile_picture | BYTEA / S3 key | Profile image |
| department | VARCHAR | Department name |
| semester | VARCHAR | Current semester |
| enrollment_number | VARCHAR | Student enrollment ID |
| faculty_id | VARCHAR | Faculty ID |
| phone_number | VARCHAR | |
| address | TEXT | |
| date_of_birth | DATE | |
| role | ENUM | ADMIN, FACULTY, STUDENT |
| enabled | BOOLEAN | Account active flag |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |

#### `courses`
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | |
| tenant_id | UUID FK | *(to add)* |
| course_code | VARCHAR | Unique per tenant |
| course_name | VARCHAR | |
| department | VARCHAR | |
| credits | INT | |
| instructor | VARCHAR | Faculty name/ID |
| semester | VARCHAR | |

#### `exams`
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | |
| tenant_id | UUID FK | *(to add)* |
| exam_code | VARCHAR | Unique |
| course_code | VARCHAR | FK to courses |
| title | VARCHAR | |
| description | TEXT | |
| start_date | TIMESTAMP | |
| end_date | TIMESTAMP | |
| type | ENUM | MIDTERM, FINAL, QUIZ, ASSIGNMENT |

#### `results`
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | |
| tenant_id | UUID FK | *(to add)* |
| student_id | VARCHAR | |
| exam_code | VARCHAR | |
| course_code | VARCHAR | |
| exam_id | BIGINT FK | |
| total_marks | DOUBLE | |
| obtained_marks | DOUBLE | |
| grade | VARCHAR | |
| status | VARCHAR | Pass/Fail/Pending |
| comments | TEXT | |
| result_date | TIMESTAMP | |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |

#### `materials`
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | |
| tenant_id | UUID FK | *(to add)* |
| material_code | VARCHAR | |
| course_code | VARCHAR | |
| title | VARCHAR | |
| description | TEXT | |
| type | VARCHAR | PDF, DOC, PPT, VIDEO, LINK |
| file_content | BYTEA / S3 key | |
| uploaded_by | VARCHAR | |
| uploaded_date | TIMESTAMP | |
| file_size | BIGINT | |
| download_count | INT | |
| last_updated_date | TIMESTAMP | |
| file_type | VARCHAR | |

#### `events`
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | |
| tenant_id | UUID FK | *(to add)* |
| title | VARCHAR | |
| description | TEXT | |
| image_content | BYTEA / S3 key | |
| posted_by | VARCHAR | |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |

### 7.3 Planned New Tables

#### `tenants` *(new)*
| Column | Type | Description |
|--------|------|-------------|
| id | UUID PK | |
| name | VARCHAR | College name |
| code | VARCHAR | Unique short code |
| logo_url | VARCHAR | S3 URL |
| timezone | VARCHAR | e.g., Asia/Kolkata |
| enabled | BOOLEAN | |
| subscription_plan | VARCHAR | BASIC, PRO, ENTERPRISE |
| created_at | TIMESTAMP | |

#### `attendance` *(new)*
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | |
| tenant_id | UUID FK | |
| student_id | BIGINT FK | users.id |
| course_code | VARCHAR | |
| session_date | DATE | |
| status | ENUM | PRESENT, ABSENT, LATE |
| marked_by | BIGINT FK | users.id (faculty) |
| created_at | TIMESTAMP | |

#### `timetable_entries` *(new)*
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | |
| tenant_id | UUID FK | |
| day_of_week | VARCHAR | MON–SUN |
| time_slot | VARCHAR | e.g., "09:00-10:00" |
| course_code | VARCHAR | |
| faculty_id | VARCHAR | |
| room_number | VARCHAR | |
| semester | VARCHAR | |
| created_at | TIMESTAMP | |

#### `departments` *(new)*
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | |
| tenant_id | UUID FK | |
| name | VARCHAR | |
| head_faculty_id | BIGINT FK | |
| created_at | TIMESTAMP | |

#### `notifications` *(new)*
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | |
| tenant_id | UUID FK | |
| user_id | BIGINT FK | |
| title | VARCHAR | |
| message | TEXT | |
| type | ENUM | EXAM, RESULT, ATTENDANCE, EVENT, GENERAL |
| is_read | BOOLEAN | |
| created_at | TIMESTAMP | |

---

## 8. API Requirements Overview

### 8.1 Base URL
`http://{host}:{port}/api`

### 8.2 Authentication Headers
All secured endpoints require:
```
Authorization: Bearer {JWT_TOKEN}
```

### 8.3 Standard Response Format
```json
{
  "success": true,
  "data": { ... },
  "message": "Operation successful",
  "timestamp": "2026-03-08T10:00:00Z"
}
```

### 8.4 Error Response Format (GlobalExceptionHandler)
```json
{
  "error": "RESOURCE_NOT_FOUND",
  "message": "User not found with id: 5",
  "status": 404,
  "timestamp": "2026-03-08T10:00:00Z"
}
```

### 8.5 Existing API Endpoints

| Controller | Base Path | Roles | Status |
|-----------|-----------|-------|--------|
| AuthController | `/api/auth/**` | Public | ✅ Implemented |
| ProfileController | `/api/profile/**` | All Authenticated | ✅ Implemented |
| CourseController | `/api/courses/**` | All Authenticated | ✅ Implemented |
| ExamController | `/api/exams/**` | FACULTY, ADMIN, STUDENT | ✅ Implemented |
| MaterialController | `/api/materials/**` | FACULTY, ADMIN, STUDENT | ✅ Implemented |
| ResultController | `/api/results/**` | FACULTY, ADMIN, STUDENT | ✅ Implemented |
| EventController | `/api/events/**` | All Authenticated | ✅ Implemented |

### 8.6 Planned API Endpoints

| Module | Base Path | Roles | Status |
|--------|-----------|-------|--------|
| OTP | `/api/otp/**` | Public | 🔲 Planned |
| Admin | `/api/admin/**` | ADMIN | 🔲 Planned |
| Attendance | `/api/attendance/**` | FACULTY, ADMIN, STUDENT | 🔲 Planned |
| Timetable | `/api/timetable/**` | ADMIN, FACULTY, STUDENT | 🔲 Planned |
| Notifications | `/api/notifications/**` | All Authenticated | 🔲 Planned |
| Tenant Management | `/api/tenants/**` | SUPER_ADMIN | 🔲 Planned |
| Department | `/api/departments/**` | ADMIN | 🔲 Planned |

---

## 9. SaaS Multi-Tenancy Requirements

### 9.1 Multi-Tenancy Strategy
**Chosen Strategy:** Shared Database + Shared Schema with `tenant_id` discriminator column

**Rationale:**
- Simpler to implement on top of existing monolith
- Cost-effective for small-to-medium scale
- All entities will have a `tenantId` (UUID) column
- Application-level tenant filtering on every query

### 9.2 Tenant Resolution
- Tenant ID shall be resolved from the JWT token claim `tenantId`
- A `TenantContext` (ThreadLocal) shall hold the current tenant per request
- A `TenantFilter` (Spring Security filter) shall populate the context

### 9.3 Data Isolation Rules
- Every JPA query shall include `WHERE tenant_id = :tenantId`
- JPA `@Filter` or custom repository methods shall enforce isolation
- Cross-tenant queries shall be forbidden at application level
- Super Admin bypass shall require explicit privileged context

### 9.4 Tenant Onboarding Flow
```
1. Super Admin calls POST /api/tenants
2. System creates Tenant record
3. System creates default ADMIN user for college
4. College Admin logs in, configures college settings
5. College Admin adds Faculty and Students
```

---

## 10. Gap Analysis

### 10.1 What's Already Built ✅

| Feature | Status | Notes |
|---------|--------|-------|
| User model (ADMIN, FACULTY, STUDENT) | ✅ Built | Needs SUPER_ADMIN role |
| JWT Authentication | ✅ Built | SecurityConfig + JwtConfig |
| Spring Security RBAC | ✅ Built | Role-based endpoint protection |
| Course CRUD | ✅ Built | Basic fields present |
| Exam Management | ✅ Built | With ExamType enum |
| Results & ResultDetail | ✅ Built | Grade, status, marks |
| Study Materials | ✅ Built | With S3 + @Lob |
| Events/Announcements | ✅ Built | Image support |
| Profile Management | ✅ Built | Student/Faculty profiles |
| AWS S3 Configuration | ✅ Built | campusconnectst, ap-south-1 |
| Swagger / OpenAPI | ✅ Built | springdoc 2.2.0 |
| Exception Handling | ✅ Built | GlobalExceptionHandler |
| Input Validation | ✅ Built | Spring Validation |

### 10.2 What's Missing / Planned 🔲

| Feature | Priority | Complexity |
|---------|----------|-----------|
| Multi-tenancy (tenant_id on all entities) | P1 | High |
| SUPER_ADMIN role | P1 | Medium |
| Tenant model & onboarding API | P1 | Medium |
| OTP module (`/api/otp/**`) | P1 | Medium |
| Attendance module | P1 | Medium |
| Timetable module | P1 | Medium |
| Department model | P1 | Low |
| Notification system | P2 | High |
| Admin management API (`/api/admin/**`) | P1 | Medium |
| Password reset flow | P2 | Medium |
| Flyway schema migrations | P2 | Low |
| Rate limiting | P2 | Low |
| Email service (SMTP/SES) | P2 | Medium |
| Reporting & analytics | P3 | High |
| Docker & CI/CD pipeline | P2 | Medium |

---

## 11. Assumptions & Dependencies

1. A separate frontend SPA (React/Vue) will consume all REST APIs
2. PostgreSQL is available locally at `localhost:5432/ccdb` for development
3. AWS S3 credentials will be injected via environment variables in production
4. OTP delivery will use email (Spring Mail + AWS SES or SMTP)
5. Initial deployment target is AWS EC2 / ECS with RDS PostgreSQL
6. Lombok, Spring Boot DevTools, and Springdoc annotations will be used consistently

---

## 12. Appendix

### 12.1 Tech Stack Summary

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 21 |
| Framework | Spring Boot | 3.4.5 |
| ORM | Spring Data JPA (Hibernate) | 6.x |
| Security | Spring Security + JWT (jjwt) | 0.12.6 |
| Database | PostgreSQL | 15+ |
| File Storage | AWS S3 SDK | 1.12.587 |
| API Docs | Springdoc OpenAPI | 2.2.0 |
| Build | Maven | 3.x |
| Code Gen | Lombok | Latest |
| Validation | Spring Boot Validation | 3.4.5 |
| Monitoring | Spring Actuator | 3.4.5 |
| Utilities | Apache Commons IO | 2.14.0 |

### 12.2 HTTP Status Code Conventions

| Code | Usage |
|------|-------|
| 200 OK | Successful GET, PUT |
| 201 Created | Successful POST (resource created) |
| 204 No Content | Successful DELETE |
| 400 Bad Request | Validation error |
| 401 Unauthorized | Missing or invalid JWT |
| 403 Forbidden | Insufficient role |
| 404 Not Found | Resource not found |
| 409 Conflict | Duplicate resource |
| 500 Internal Server Error | Unexpected server error |

