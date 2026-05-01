# API Reference

Generated from controller annotations in the backend.

## Auth

Most endpoints require a Bearer token in `Authorization` header. Public endpoints are under `/api/auth/**`, `/api/otp/**`, `/api/dev/**`, `/swagger-ui/**`, `/v3/api-docs/**`, `/api/docs/**`.

## Endpoints

### `GET /api/academic-years`
- Controller: `AcademicYearController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<AcademicYear>`

### `POST /api/academic-years`
- Controller: `AcademicYearController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `AcademicYear`
- Response: `AcademicYear`

### `GET /api/academic-years/current`
- Controller: `AcademicYearController.java` / method `getCurrentAcademicYear`
- Path params: None
- Query params: None
- Request body: None
- Response: `AcademicYear`

### `GET /api/academic-years/label/{label}`
- Controller: `AcademicYearController.java` / method `getByLabel`
- Path params: label: String
- Query params: None
- Request body: None
- Response: `AcademicYear`

### `GET /api/academic-years/public-id/{publicId}`
- Controller: `AcademicYearController.java` / method `getByPublicId`
- Path params: publicId: UUID
- Query params: None
- Request body: None
- Response: `AcademicYear`

### `DELETE /api/academic-years/{id}`
- Controller: `AcademicYearController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/academic-years/{id}`
- Controller: `AcademicYearController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `AcademicYear`

### `PUT /api/academic-years/{id}`
- Controller: `AcademicYearController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `AcademicYear`
- Response: `AcademicYear`

### `POST /api/admin/maintenance/backfill-canonical-ids`
- Controller: `AdminMaintenanceController.java` / method `backfillCanonicalIds`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, Object>`

### `GET /api/admin/stats`
- Controller: `AdminController.java` / method `getDashboardStats`
- Path params: None
- Query params: None
- Request body: None
- Response: `AdminDashboardStats`

### `GET /api/admin/students`
- Controller: `AdminController.java` / method `getStudents`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<User>`

### `GET /api/admin/users`
- Controller: `AdminController.java` / method `getAllUsers`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<User>`

### `POST /api/admin/users`
- Controller: `AdminController.java` / method `createUser`
- Path params: None
- Query params: None
- Request body: `AdminUserRequest`
- Response: `User`

### `DELETE /api/admin/users/id/{id}`
- Controller: `AdminController.java` / method `deleteUserById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `GET /api/admin/users/id/{id}`
- Controller: `AdminController.java` / method `getUserByNumericId`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `User`

### `PUT /api/admin/users/id/{id}`
- Controller: `AdminController.java` / method `updateUserById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `User`

### `PUT /api/admin/users/id/{id}/disable`
- Controller: `AdminController.java` / method `disableUserById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `PUT /api/admin/users/id/{id}/enable`
- Controller: `AdminController.java` / method `enableUserById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `GET /api/admin/users/role/{role}`
- Controller: `AdminController.java` / method `getUsersByRole`
- Path params: role: String
- Query params: None
- Request body: None
- Response: `List<User>`

### `DELETE /api/admin/users/{publicId}`
- Controller: `AdminController.java` / method `deleteUser`
- Path params: publicId: UUID
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `GET /api/admin/users/{publicId}`
- Controller: `AdminController.java` / method `getUserById`
- Path params: publicId: UUID
- Query params: None
- Request body: None
- Response: `User`

### `PUT /api/admin/users/{publicId}`
- Controller: `AdminController.java` / method `updateUser`
- Path params: publicId: UUID
- Query params: None
- Request body: None
- Response: `User`

### `PUT /api/admin/users/{publicId}/disable`
- Controller: `AdminController.java` / method `disableUser`
- Path params: publicId: UUID
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `PUT /api/admin/users/{publicId}/enable`
- Controller: `AdminController.java` / method `enableUser`
- Path params: publicId: UUID
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `GET /api/announcements`
- Controller: `AnnouncementController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Announcement>`

### `POST /api/announcements`
- Controller: `AnnouncementController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Announcement`
- Response: `Announcement`

### `GET /api/announcements/active`
- Controller: `AnnouncementController.java` / method `getActive`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Announcement>`

### `GET /api/announcements/tenant/{tenantId}`
- Controller: `AnnouncementController.java` / method `getByTenantId`
- Path params: tenantId: UUID
- Query params: None
- Request body: None
- Response: `List<Announcement>`

### `DELETE /api/announcements/{id}`
- Controller: `AnnouncementController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/announcements/{id}`
- Controller: `AnnouncementController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Announcement`

### `PUT /api/announcements/{id}`
- Controller: `AnnouncementController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Announcement`
- Response: `Announcement`

### `GET /api/attendance-sessions`
- Controller: `AttendanceSessionController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<AttendanceSession>`

### `POST /api/attendance-sessions`
- Controller: `AttendanceSessionController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `AttendanceSession`
- Response: `AttendanceSession`

### `GET /api/attendance-sessions/course-assignment/{courseAssignmentId}`
- Controller: `AttendanceSessionController.java` / method `getByCourseAssignmentId`
- Path params: courseAssignmentId: Long
- Query params: None
- Request body: None
- Response: `List<AttendanceSession>`

### `GET /api/attendance-sessions/faculty/{facultyId}`
- Controller: `AttendanceSessionController.java` / method `getByConductedBy`
- Path params: facultyId: Long
- Query params: None
- Request body: None
- Response: `List<AttendanceSession>`

### `DELETE /api/attendance-sessions/{id}`
- Controller: `AttendanceSessionController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/attendance-sessions/{id}`
- Controller: `AttendanceSessionController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `AttendanceSession`

### `PUT /api/attendance-sessions/{id}`
- Controller: `AttendanceSessionController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `AttendanceSession`
- Response: `AttendanceSession`

### `POST /api/attendance/bulk`
- Controller: `AttendanceController.java` / method `bulkCreate`
- Path params: None
- Query params: None
- Request body: `List<Map<String, Object>>`
- Response: `Map<String, Object>`

### `GET /api/attendance/course/{courseCode}`
- Controller: `AttendanceController.java` / method `getByCourse`
- Path params: courseCode: String
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `GET /api/attendance/percentage/{studentId}/course/{courseCode}`
- Controller: `AttendanceController.java` / method `percentage`
- Path params: studentId: Long, courseCode: String
- Query params: None
- Request body: None
- Response: `Map<String, Object>`

### `GET /api/attendance/student/{studentId}`
- Controller: `AttendanceController.java` / method `getByStudent`
- Path params: studentId: Long
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `GET /api/attendance/student/{studentId}/course/{courseCode}`
- Controller: `AttendanceController.java` / method `getByStudentAndCourse`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `POST /api/auth/login`
- Controller: `AuthController.java` / method `login`
- Path params: None
- Query params: None
- Request body: `AuthRequest`
- Response: `AuthResponse`

### `POST /api/auth/register`
- Controller: `AuthController.java` / method `register`
- Path params: None
- Query params: None
- Request body: `AuthRequest`
- Response: `AuthResponse`

### `GET /api/batches`
- Controller: `BatchController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Batch>`

### `POST /api/batches`
- Controller: `BatchController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Batch`
- Response: `Batch`

### `GET /api/batches/program/{programId}`
- Controller: `BatchController.java` / method `getByProgramId`
- Path params: programId: Long
- Query params: None
- Request body: None
- Response: `List<Batch>`

### `GET /api/batches/public-id/{publicId}`
- Controller: `BatchController.java` / method `getByPublicId`
- Path params: publicId: UUID
- Query params: None
- Request body: None
- Response: `Batch`

### `DELETE /api/batches/{id}`
- Controller: `BatchController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/batches/{id}`
- Controller: `BatchController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Batch`

### `PUT /api/batches/{id}`
- Controller: `BatchController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Batch`
- Response: `Batch`

### `GET /api/canonical/mongo/tenants/{tenantId}/materials`
- Controller: `MaterialDocumentController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<MaterialDocumentResponse>`

### `POST /api/canonical/mongo/tenants/{tenantId}/materials`
- Controller: `MaterialDocumentController.java` / method `create`
- Path params: None
- Query params: None
- Request body: None
- Response: `MaterialDocumentResponse`

### `DELETE /api/canonical/mongo/tenants/{tenantId}/materials/{publicId}`
- Controller: `MaterialDocumentController.java` / method `delete`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `GET /api/canonical/mongo/tenants/{tenantId}/materials/{publicId}`
- Controller: `MaterialDocumentController.java` / method `getByPublicId`
- Path params: None
- Query params: None
- Request body: None
- Response: `MaterialDocumentResponse`

### `PUT /api/canonical/mongo/tenants/{tenantId}/materials/{publicId}`
- Controller: `MaterialDocumentController.java` / method `update`
- Path params: None
- Query params: None
- Request body: None
- Response: `MaterialDocumentResponse`

### `GET /api/canonical/mongo/tenants/{tenantId}/notifications`
- Controller: `NotificationDocumentController.java` / method `getAll`
- Path params: tenantId: String
- Query params: None
- Request body: None
- Response: `List<NotificationDocumentResponse>`

### `POST /api/canonical/mongo/tenants/{tenantId}/notifications`
- Controller: `NotificationDocumentController.java` / method `create`
- Path params: None
- Query params: None
- Request body: None
- Response: `NotificationDocumentResponse`

### `DELETE /api/canonical/mongo/tenants/{tenantId}/notifications/{publicId}`
- Controller: `NotificationDocumentController.java` / method `delete`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `GET /api/canonical/mongo/tenants/{tenantId}/notifications/{publicId}`
- Controller: `NotificationDocumentController.java` / method `getByPublicId`
- Path params: None
- Query params: None
- Request body: None
- Response: `NotificationDocumentResponse`

### `PUT /api/canonical/mongo/tenants/{tenantId}/notifications/{publicId}`
- Controller: `NotificationDocumentController.java` / method `update`
- Path params: None
- Query params: None
- Request body: None
- Response: `NotificationDocumentResponse`

### `GET /api/canonical/postgres/tenants`
- Controller: `TenantCanonicalController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<TenantCanonicalResponse>`

### `POST /api/canonical/postgres/tenants`
- Controller: `TenantCanonicalController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `TenantCanonicalRequest`
- Response: `TenantCanonicalResponse`

### `GET /api/canonical/postgres/tenants/{tenantId}/courses`
- Controller: `CourseCanonicalController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<CourseCanonicalResponse>`

### `POST /api/canonical/postgres/tenants/{tenantId}/courses`
- Controller: `CourseCanonicalController.java` / method `create`
- Path params: None
- Query params: None
- Request body: None
- Response: `CourseCanonicalResponse`

### `DELETE /api/canonical/postgres/tenants/{tenantId}/courses/{coursePublicId}`
- Controller: `CourseCanonicalController.java` / method `delete`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `GET /api/canonical/postgres/tenants/{tenantId}/courses/{coursePublicId}`
- Controller: `CourseCanonicalController.java` / method `getByPublicId`
- Path params: None
- Query params: None
- Request body: None
- Response: `CourseCanonicalResponse`

### `PUT /api/canonical/postgres/tenants/{tenantId}/courses/{coursePublicId}`
- Controller: `CourseCanonicalController.java` / method `update`
- Path params: None
- Query params: None
- Request body: None
- Response: `CourseCanonicalResponse`

### `GET /api/canonical/postgres/tenants/{tenantId}/departments`
- Controller: `DepartmentCanonicalController.java` / method `getAll`
- Path params: tenantId: UUID
- Query params: None
- Request body: None
- Response: `List<DepartmentCanonicalResponse>`

### `POST /api/canonical/postgres/tenants/{tenantId}/departments`
- Controller: `DepartmentCanonicalController.java` / method `create`
- Path params: None
- Query params: None
- Request body: None
- Response: `DepartmentCanonicalResponse`

### `DELETE /api/canonical/postgres/tenants/{tenantId}/departments/{departmentPublicId}`
- Controller: `DepartmentCanonicalController.java` / method `delete`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `GET /api/canonical/postgres/tenants/{tenantId}/departments/{departmentPublicId}`
- Controller: `DepartmentCanonicalController.java` / method `getByPublicId`
- Path params: None
- Query params: None
- Request body: None
- Response: `DepartmentCanonicalResponse`

### `PUT /api/canonical/postgres/tenants/{tenantId}/departments/{departmentPublicId}`
- Controller: `DepartmentCanonicalController.java` / method `update`
- Path params: None
- Query params: None
- Request body: None
- Response: `DepartmentCanonicalResponse`

### `DELETE /api/canonical/postgres/tenants/{tenantPublicId}`
- Controller: `TenantCanonicalController.java` / method `delete`
- Path params: tenantPublicId: UUID
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `GET /api/canonical/postgres/tenants/{tenantPublicId}`
- Controller: `TenantCanonicalController.java` / method `getByPublicId`
- Path params: tenantPublicId: UUID
- Query params: None
- Request body: None
- Response: `TenantCanonicalResponse`

### `PUT /api/canonical/postgres/tenants/{tenantPublicId}`
- Controller: `TenantCanonicalController.java` / method `update`
- Path params: None
- Query params: None
- Request body: None
- Response: `TenantCanonicalResponse`

### `GET /api/course-assignments`
- Controller: `CourseAssignmentController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<CourseAssignment>`

### `POST /api/course-assignments`
- Controller: `CourseAssignmentController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `CourseAssignment`
- Response: `CourseAssignment`

### `GET /api/course-assignments/course/{courseId}`
- Controller: `CourseAssignmentController.java` / method `getByCourseId`
- Path params: courseId: Long
- Query params: None
- Request body: None
- Response: `List<CourseAssignment>`

### `GET /api/course-assignments/faculty/{facultyId}`
- Controller: `CourseAssignmentController.java` / method `getByFacultyId`
- Path params: facultyId: Long
- Query params: None
- Request body: None
- Response: `List<CourseAssignment>`

### `DELETE /api/course-assignments/{id}`
- Controller: `CourseAssignmentController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/course-assignments/{id}`
- Controller: `CourseAssignmentController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `CourseAssignment`

### `PUT /api/course-assignments/{id}`
- Controller: `CourseAssignmentController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `CourseAssignment`
- Response: `CourseAssignment`

### `GET /api/courses`
- Controller: `CourseController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `POST /api/courses`
- Controller: `CourseController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Map<String, Object>`
- Response: `Map<String, Object>`

### `DELETE /api/courses/{id}`
- Controller: `CourseController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `PUT /api/courses/{id}`
- Controller: `CourseController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Map<String, Object>`
- Response: `Map<String, Object>`

### `GET /api/departments`
- Controller: `DepartmentController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `POST /api/departments`
- Controller: `DepartmentController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `DepartmentCanonical`
- Response: `Map<String, Object>`

### `DELETE /api/departments/{id}`
- Controller: `DepartmentController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `PUT /api/departments/{id}`
- Controller: `DepartmentController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `DepartmentCanonical`
- Response: `Map<String, Object>`

### `GET /api/dev/hash`
- Controller: `DevController.java` / method `getHash`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `POST /api/dev/reset-passwords`
- Controller: `DevController.java` / method `resetPasswords`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, Object>`

### `GET /api/documents/audit-logs`
- Controller: `AuditLogDocumentController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<AuditLogDocument>`

### `POST /api/documents/audit-logs`
- Controller: `AuditLogDocumentController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `AuditLogDocument`
- Response: `AuditLogDocument`

### `GET /api/documents/audit-logs/resource-type/{resourceType}`
- Controller: `AuditLogDocumentController.java` / method `getByResourceType`
- Path params: resourceType: String
- Query params: None
- Request body: None
- Response: `List<AuditLogDocument>`

### `GET /api/documents/audit-logs/tenant/{tenantId}`
- Controller: `AuditLogDocumentController.java` / method `getByTenantId`
- Path params: tenantId: String
- Query params: None
- Request body: None
- Response: `List<AuditLogDocument>`

### `DELETE /api/documents/audit-logs/{id}`
- Controller: `AuditLogDocumentController.java` / method `delete`
- Path params: id: String
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/documents/audit-logs/{id}`
- Controller: `AuditLogDocumentController.java` / method `getById`
- Path params: id: String
- Query params: None
- Request body: None
- Response: `AuditLogDocument`

### `PUT /api/documents/audit-logs/{id}`
- Controller: `AuditLogDocumentController.java` / method `update`
- Path params: id: String
- Query params: None
- Request body: `AuditLogDocument`
- Response: `AuditLogDocument`

### `GET /api/documents/chat-messages`
- Controller: `ChatMessageDocumentController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<ChatMessageDocument>`

### `POST /api/documents/chat-messages`
- Controller: `ChatMessageDocumentController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `ChatMessageDocument`
- Response: `ChatMessageDocument`

### `GET /api/documents/chat-messages/channel/{channelId}`
- Controller: `ChatMessageDocumentController.java` / method `getByChannelId`
- Path params: channelId: String
- Query params: None
- Request body: None
- Response: `List<ChatMessageDocument>`

### `GET /api/documents/chat-messages/public-id`
- Controller: `ChatMessageDocumentController.java` / method `getByPublicId`
- Path params: None
- Query params: None
- Request body: None
- Response: `ChatMessageDocument`

### `GET /api/documents/chat-messages/sender/{senderId}`
- Controller: `ChatMessageDocumentController.java` / method `getBySenderId`
- Path params: senderId: Long
- Query params: None
- Request body: None
- Response: `List<ChatMessageDocument>`

### `GET /api/documents/chat-messages/tenant-channel`
- Controller: `ChatMessageDocumentController.java` / method `getByTenantAndChannel`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<ChatMessageDocument>`

### `DELETE /api/documents/chat-messages/{id}`
- Controller: `ChatMessageDocumentController.java` / method `delete`
- Path params: id: String
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/documents/chat-messages/{id}`
- Controller: `ChatMessageDocumentController.java` / method `getById`
- Path params: id: String
- Query params: None
- Request body: None
- Response: `ChatMessageDocument`

### `PUT /api/documents/chat-messages/{id}`
- Controller: `ChatMessageDocumentController.java` / method `update`
- Path params: id: String
- Query params: None
- Request body: `ChatMessageDocument`
- Response: `ChatMessageDocument`

### `GET /api/documents/report-snapshots`
- Controller: `ReportSnapshotDocumentController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<ReportSnapshotDocument>`

### `POST /api/documents/report-snapshots`
- Controller: `ReportSnapshotDocumentController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `ReportSnapshotDocument`
- Response: `ReportSnapshotDocument`

### `GET /api/documents/report-snapshots/public-id`
- Controller: `ReportSnapshotDocumentController.java` / method `getByPublicId`
- Path params: None
- Query params: None
- Request body: None
- Response: `ReportSnapshotDocument`

### `GET /api/documents/report-snapshots/report-type/{reportType}`
- Controller: `ReportSnapshotDocumentController.java` / method `getByReportType`
- Path params: reportType: String
- Query params: None
- Request body: None
- Response: `List<ReportSnapshotDocument>`

### `GET /api/documents/report-snapshots/tenant/{tenantId}`
- Controller: `ReportSnapshotDocumentController.java` / method `getByTenantId`
- Path params: tenantId: String
- Query params: None
- Request body: None
- Response: `List<ReportSnapshotDocument>`

### `DELETE /api/documents/report-snapshots/{id}`
- Controller: `ReportSnapshotDocumentController.java` / method `delete`
- Path params: id: String
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/documents/report-snapshots/{id}`
- Controller: `ReportSnapshotDocumentController.java` / method `getById`
- Path params: id: String
- Query params: None
- Request body: None
- Response: `ReportSnapshotDocument`

### `PUT /api/documents/report-snapshots/{id}`
- Controller: `ReportSnapshotDocumentController.java` / method `update`
- Path params: id: String
- Query params: None
- Request body: `ReportSnapshotDocument`
- Response: `ReportSnapshotDocument`

### `GET /api/events`
- Controller: `EventController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `POST /api/events`
- Controller: `EventController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Map<String, Object>`
- Response: `Map<String, Object>`

### `DELETE /api/events/{id}`
- Controller: `EventController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `PUT /api/events/{id}`
- Controller: `EventController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Map<String, Object>`
- Response: `Map<String, Object>`

### `GET /api/exam-schedules`
- Controller: `ExamScheduleController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<ExamSchedule>`

### `POST /api/exam-schedules`
- Controller: `ExamScheduleController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `ExamSchedule`
- Response: `ExamSchedule`

### `GET /api/exam-schedules/exam/{examId}`
- Controller: `ExamScheduleController.java` / method `getByExamId`
- Path params: examId: Long
- Query params: None
- Request body: None
- Response: `List<ExamSchedule>`

### `GET /api/exam-schedules/room/{roomId}`
- Controller: `ExamScheduleController.java` / method `getByRoomId`
- Path params: roomId: Long
- Query params: None
- Request body: None
- Response: `List<ExamSchedule>`

### `DELETE /api/exam-schedules/{id}`
- Controller: `ExamScheduleController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/exam-schedules/{id}`
- Controller: `ExamScheduleController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `ExamSchedule`

### `PUT /api/exam-schedules/{id}`
- Controller: `ExamScheduleController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `ExamSchedule`
- Response: `ExamSchedule`

### `GET /api/exams`
- Controller: `ExamController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `POST /api/exams`
- Controller: `ExamController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Map<String, Object>`
- Response: `Map<String, Object>`

### `DELETE /api/exams/{examCode}`
- Controller: `ExamController.java` / method `delete`
- Path params: examCode: String
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `DELETE /api/exams/{examCode}/enroll`
- Controller: `ExamController.java` / method `unenroll`
- Path params: examCode: String
- Query params: studentId: Long
- Request body: None
- Response: `Map<String, Object>`

### `POST /api/exams/{examCode}/enroll`
- Controller: `ExamController.java` / method `enroll`
- Path params: examCode: String
- Query params: studentId: Long
- Request body: None
- Response: `Map<String, Object>`

### `PUT /api/exams/{id}`
- Controller: `ExamController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Map<String, Object>`
- Response: `Map<String, Object>`

### `GET /api/faculty`
- Controller: `FacultyController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Faculty>`

### `POST /api/faculty`
- Controller: `FacultyController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Faculty`
- Response: `Faculty`

### `GET /api/faculty-education`
- Controller: `FacultyEducationController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<FacultyEducation>`

### `POST /api/faculty-education`
- Controller: `FacultyEducationController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `FacultyEducation`
- Response: `FacultyEducation`

### `GET /api/faculty-education/faculty/{facultyId}`
- Controller: `FacultyEducationController.java` / method `getByFacultyId`
- Path params: facultyId: Long
- Query params: None
- Request body: None
- Response: `List<FacultyEducation>`

### `DELETE /api/faculty-education/{id}`
- Controller: `FacultyEducationController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/faculty-education/{id}`
- Controller: `FacultyEducationController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `FacultyEducation`

### `PUT /api/faculty-education/{id}`
- Controller: `FacultyEducationController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `FacultyEducation`
- Response: `FacultyEducation`

### `GET /api/faculty-work-experience`
- Controller: `FacultyWorkExperienceController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<FacultyWorkExperience>`

### `POST /api/faculty-work-experience`
- Controller: `FacultyWorkExperienceController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `FacultyWorkExperience`
- Response: `FacultyWorkExperience`

### `GET /api/faculty-work-experience/faculty/{facultyId}`
- Controller: `FacultyWorkExperienceController.java` / method `getByFacultyId`
- Path params: facultyId: Long
- Query params: None
- Request body: None
- Response: `List<FacultyWorkExperience>`

### `DELETE /api/faculty-work-experience/{id}`
- Controller: `FacultyWorkExperienceController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/faculty-work-experience/{id}`
- Controller: `FacultyWorkExperienceController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `FacultyWorkExperience`

### `PUT /api/faculty-work-experience/{id}`
- Controller: `FacultyWorkExperienceController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `FacultyWorkExperience`
- Response: `FacultyWorkExperience`

### `GET /api/faculty/department/{departmentId}`
- Controller: `FacultyController.java` / method `getByDepartmentId`
- Path params: departmentId: Long
- Query params: None
- Request body: None
- Response: `List<Faculty>`

### `GET /api/faculty/public-id/{publicId}`
- Controller: `FacultyController.java` / method `getByPublicId`
- Path params: publicId: UUID
- Query params: None
- Request body: None
- Response: `Faculty`

### `DELETE /api/faculty/{id}`
- Controller: `FacultyController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/faculty/{id}`
- Controller: `FacultyController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Faculty`

### `PUT /api/faculty/{id}`
- Controller: `FacultyController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Faculty`
- Response: `Faculty`

### `GET /api/fee-invoices`
- Controller: `FeeInvoiceController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<FeeInvoice>`

### `POST /api/fee-invoices`
- Controller: `FeeInvoiceController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `FeeInvoice`
- Response: `FeeInvoice`

### `GET /api/fee-invoices/status/{status}`
- Controller: `FeeInvoiceController.java` / method `getByStatus`
- Path params: status: String
- Query params: None
- Request body: None
- Response: `List<FeeInvoice>`

### `GET /api/fee-invoices/student/{studentId}`
- Controller: `FeeInvoiceController.java` / method `getByStudentId`
- Path params: studentId: Long
- Query params: None
- Request body: None
- Response: `List<FeeInvoice>`

### `DELETE /api/fee-invoices/{id}`
- Controller: `FeeInvoiceController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/fee-invoices/{id}`
- Controller: `FeeInvoiceController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `FeeInvoice`

### `PUT /api/fee-invoices/{id}`
- Controller: `FeeInvoiceController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `FeeInvoice`
- Response: `FeeInvoice`

### `GET /api/fee-payments`
- Controller: `FeePaymentController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<FeePayment>`

### `POST /api/fee-payments`
- Controller: `FeePaymentController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `FeePayment`
- Response: `FeePayment`

### `GET /api/fee-payments/status/{status}`
- Controller: `FeePaymentController.java` / method `getByStatus`
- Path params: status: String
- Query params: None
- Request body: None
- Response: `List<FeePayment>`

### `GET /api/fee-payments/student/{studentId}`
- Controller: `FeePaymentController.java` / method `getByStudentId`
- Path params: studentId: Long
- Query params: None
- Request body: None
- Response: `List<FeePayment>`

### `DELETE /api/fee-payments/{id}`
- Controller: `FeePaymentController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/fee-payments/{id}`
- Controller: `FeePaymentController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `FeePayment`

### `PUT /api/fee-payments/{id}`
- Controller: `FeePaymentController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `FeePayment`
- Response: `FeePayment`

### `GET /api/fee-structures`
- Controller: `FeeStructureController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<FeeStructure>`

### `POST /api/fee-structures`
- Controller: `FeeStructureController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `FeeStructure`
- Response: `FeeStructure`

### `GET /api/fee-structures/program/{programId}`
- Controller: `FeeStructureController.java` / method `getByProgramId`
- Path params: programId: Long
- Query params: None
- Request body: None
- Response: `List<FeeStructure>`

### `GET /api/fee-structures/tenant/{tenantId}`
- Controller: `FeeStructureController.java` / method `getByTenantId`
- Path params: tenantId: UUID
- Query params: None
- Request body: None
- Response: `List<FeeStructure>`

### `DELETE /api/fee-structures/{id}`
- Controller: `FeeStructureController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/fee-structures/{id}`
- Controller: `FeeStructureController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `FeeStructure`

### `PUT /api/fee-structures/{id}`
- Controller: `FeeStructureController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `FeeStructure`
- Response: `FeeStructure`

### `GET /api/holidays`
- Controller: `HolidayController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Holiday>`

### `POST /api/holidays`
- Controller: `HolidayController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Holiday`
- Response: `Holiday`

### `GET /api/holidays/tenant/{tenantId}`
- Controller: `HolidayController.java` / method `getByTenantId`
- Path params: tenantId: UUID
- Query params: None
- Request body: None
- Response: `List<Holiday>`

### `DELETE /api/holidays/{id}`
- Controller: `HolidayController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/holidays/{id}`
- Controller: `HolidayController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Holiday`

### `PUT /api/holidays/{id}`
- Controller: `HolidayController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Holiday`
- Response: `Holiday`

### `GET /api/leave-applications`
- Controller: `LeaveApplicationController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<LeaveApplication>`

### `POST /api/leave-applications`
- Controller: `LeaveApplicationController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `LeaveApplication`
- Response: `LeaveApplication`

### `GET /api/leave-applications/status/{status}`
- Controller: `LeaveApplicationController.java` / method `getByApprovalStatus`
- Path params: status: String
- Query params: None
- Request body: None
- Response: `List<LeaveApplication>`

### `GET /api/leave-applications/user/{userId}`
- Controller: `LeaveApplicationController.java` / method `getByApplicantUserId`
- Path params: userId: Long
- Query params: None
- Request body: None
- Response: `List<LeaveApplication>`

### `DELETE /api/leave-applications/{id}`
- Controller: `LeaveApplicationController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/leave-applications/{id}`
- Controller: `LeaveApplicationController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `LeaveApplication`

### `PUT /api/leave-applications/{id}`
- Controller: `LeaveApplicationController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `LeaveApplication`
- Response: `LeaveApplication`

### `GET /api/marks`
- Controller: `MarkController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Mark>`

### `POST /api/marks`
- Controller: `MarkController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Mark`
- Response: `Mark`

### `GET /api/marks/exam/{examId}`
- Controller: `MarkController.java` / method `getByExamId`
- Path params: examId: Long
- Query params: None
- Request body: None
- Response: `List<Mark>`

### `GET /api/marks/student/{studentId}`
- Controller: `MarkController.java` / method `getByStudentId`
- Path params: studentId: Long
- Query params: None
- Request body: None
- Response: `List<Mark>`

### `DELETE /api/marks/{id}`
- Controller: `MarkController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/marks/{id}`
- Controller: `MarkController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Mark`

### `PUT /api/marks/{id}`
- Controller: `MarkController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Mark`
- Response: `Mark`

### `GET /api/materials`
- Controller: `MaterialController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `POST /api/materials/multipart/form-data`
- Controller: `MaterialController.java` / method `create`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, Object>`

### `GET /api/materials/recent/{count}`
- Controller: `MaterialController.java` / method `recent`
- Path params: count: int
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `DELETE /api/materials/{id}`
- Controller: `MaterialController.java` / method `delete`
- Path params: id: String
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `GET /api/notifications`
- Controller: `NotificationController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `PUT /api/notifications/read-all`
- Controller: `NotificationController.java` / method `readAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, Object>`

### `GET /api/notifications/unread`
- Controller: `NotificationController.java` / method `unread`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `GET /api/notifications/unread/count`
- Controller: `NotificationController.java` / method `unreadCount`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, Object>`

### `DELETE /api/notifications/{id}`
- Controller: `NotificationController.java` / method `delete`
- Path params: id: String
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `PUT /api/notifications/{id}/read`
- Controller: `NotificationController.java` / method `markRead`
- Path params: id: String
- Query params: None
- Request body: None
- Response: `Map<String, Object>`

### `POST /api/otp/resend`
- Controller: `OtpController.java` / method `resendOtp`
- Path params: None
- Query params: None
- Request body: `OtpRequest`
- Response: `Map<String, String>`

### `POST /api/otp/reset-password`
- Controller: `OtpController.java` / method `resetPassword`
- Path params: None
- Query params: None
- Request body: `OtpVerifyRequest`
- Response: `Map<String, String>`

### `POST /api/otp/send`
- Controller: `OtpController.java` / method `sendOtp`
- Path params: None
- Query params: None
- Request body: `OtpRequest`
- Response: `Map<String, String>`

### `POST /api/otp/verify`
- Controller: `OtpController.java` / method `verifyOtp`
- Path params: None
- Query params: None
- Request body: `OtpVerifyRequest`
- Response: `Map<String, String>`

### `GET /api/permissions`
- Controller: `PermissionController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Permission>`

### `POST /api/permissions`
- Controller: `PermissionController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Permission`
- Response: `Permission`

### `GET /api/permissions/module/{module}/action/{action}`
- Controller: `PermissionController.java` / method `getByModuleAndAction`
- Path params: module: String, action: String
- Query params: None
- Request body: None
- Response: `Permission`

### `DELETE /api/permissions/{id}`
- Controller: `PermissionController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/permissions/{id}`
- Controller: `PermissionController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Permission`

### `PUT /api/permissions/{id}`
- Controller: `PermissionController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Permission`
- Response: `Permission`

### `GET /api/profile`
- Controller: `ProfileController.java` / method `getMyProfile`
- Path params: None
- Query params: None
- Request body: None
- Response: `User`

### `PUT /api/profile/multipart/form-data`
- Controller: `ProfileController.java` / method `updateProfile`
- Path params: None
- Query params: None
- Request body: None
- Response: `User`

### `PUT /api/profile/password`
- Controller: `ProfileController.java` / method `changePassword`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `DELETE /api/profile/picture`
- Controller: `ProfileController.java` / method `deleteProfilePicture`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `GET /api/profile/{userId}`
- Controller: `ProfileController.java` / method `getProfile`
- Path params: userId: Long
- Query params: None
- Request body: None
- Response: `User`

### `GET /api/program-courses`
- Controller: `ProgramCourseController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<ProgramCourse>`

### `POST /api/program-courses`
- Controller: `ProgramCourseController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `ProgramCourse`
- Response: `ProgramCourse`

### `GET /api/program-courses/course/{courseId}`
- Controller: `ProgramCourseController.java` / method `getByCourseId`
- Path params: courseId: Long
- Query params: None
- Request body: None
- Response: `List<ProgramCourse>`

### `GET /api/program-courses/program/{programId}`
- Controller: `ProgramCourseController.java` / method `getByProgramId`
- Path params: programId: Long
- Query params: None
- Request body: None
- Response: `List<ProgramCourse>`

### `DELETE /api/program-courses/{id}`
- Controller: `ProgramCourseController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/program-courses/{id}`
- Controller: `ProgramCourseController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `ProgramCourse`

### `PUT /api/program-courses/{id}`
- Controller: `ProgramCourseController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `ProgramCourse`
- Response: `ProgramCourse`

### `GET /api/programs`
- Controller: `ProgramController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Program>`

### `POST /api/programs`
- Controller: `ProgramController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Program`
- Response: `Program`

### `GET /api/programs/department/{departmentId}`
- Controller: `ProgramController.java` / method `getByDepartmentId`
- Path params: departmentId: Long
- Query params: None
- Request body: None
- Response: `List<Program>`

### `GET /api/programs/public-id/{publicId}`
- Controller: `ProgramController.java` / method `getByPublicId`
- Path params: publicId: UUID
- Query params: None
- Request body: None
- Response: `Program`

### `DELETE /api/programs/{id}`
- Controller: `ProgramController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/programs/{id}`
- Controller: `ProgramController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Program`

### `PUT /api/programs/{id}`
- Controller: `ProgramController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Program`
- Response: `Program`

### `GET /api/results`
- Controller: `ResultsController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `POST /api/results`
- Controller: `ResultsController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Map<String, Object>`
- Response: `Map<String, Object>`

### `GET /api/results/student/{studentId}`
- Controller: `ResultsController.java` / method `getByStudent`
- Path params: studentId: Long
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `PUT /api/results/{id}`
- Controller: `ResultsController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Map<String, Object>`
- Response: `Map<String, Object>`

### `PUT /api/results/{id}/status`
- Controller: `ResultsController.java` / method `updateStatus`
- Path params: id: Long
- Query params: None
- Request body: `Map<String, Object>`
- Response: `Map<String, Object>`

### `GET /api/role-permissions`
- Controller: `RolePermissionController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<RolePermission>`

### `POST /api/role-permissions`
- Controller: `RolePermissionController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `RolePermission`
- Response: `RolePermission`

### `GET /api/role-permissions/permission/{permissionId}`
- Controller: `RolePermissionController.java` / method `getByPermissionId`
- Path params: permissionId: Long
- Query params: None
- Request body: None
- Response: `List<RolePermission>`

### `GET /api/role-permissions/role/{roleId}`
- Controller: `RolePermissionController.java` / method `getByRoleId`
- Path params: roleId: Long
- Query params: None
- Request body: None
- Response: `List<RolePermission>`

### `DELETE /api/role-permissions/{id}`
- Controller: `RolePermissionController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/role-permissions/{id}`
- Controller: `RolePermissionController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `RolePermission`

### `PUT /api/role-permissions/{id}`
- Controller: `RolePermissionController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `RolePermission`
- Response: `RolePermission`

### `GET /api/roles`
- Controller: `RoleController.java` / method `getAllRoles`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Role>`

### `GET /api/roles/user/{userPublicId}`
- Controller: `RoleController.java` / method `getUserRoles`
- Path params: userPublicId: UUID
- Query params: None
- Request body: None
- Response: `List<Role>`

### `POST /api/roles/user/{userPublicId}/assign`
- Controller: `RoleController.java` / method `assignRoles`
- Path params: userPublicId: UUID
- Query params: None
- Request body: None
- Response: `User`

### `DELETE /api/roles/user/{userPublicId}/remove/{roleName}`
- Controller: `RoleController.java` / method `removeRole`
- Path params: None
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `PUT /api/roles/user/{userPublicId}/set`
- Controller: `RoleController.java` / method `setRoles`
- Path params: userPublicId: UUID
- Query params: None
- Request body: None
- Response: `User`

### `GET /api/rooms`
- Controller: `RoomController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Room>`

### `POST /api/rooms`
- Controller: `RoomController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Room`
- Response: `Room`

### `GET /api/rooms/building/{building}`
- Controller: `RoomController.java` / method `getByBuilding`
- Path params: building: String
- Query params: None
- Request body: None
- Response: `List<Room>`

### `GET /api/rooms/room-number/{roomNumber}`
- Controller: `RoomController.java` / method `getByRoomNumber`
- Path params: roomNumber: String
- Query params: None
- Request body: None
- Response: `Room`

### `GET /api/rooms/type/{roomType}`
- Controller: `RoomController.java` / method `getByRoomType`
- Path params: roomType: String
- Query params: None
- Request body: None
- Response: `List<Room>`

### `DELETE /api/rooms/{id}`
- Controller: `RoomController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/rooms/{id}`
- Controller: `RoomController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Room`

### `PUT /api/rooms/{id}`
- Controller: `RoomController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Room`
- Response: `Room`

### `GET /api/students`
- Controller: `StudentController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Student>`

### `POST /api/students`
- Controller: `StudentController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Student`
- Response: `Student`

### `GET /api/students/batch/{batchId}`
- Controller: `StudentController.java` / method `getByBatchId`
- Path params: batchId: Long
- Query params: None
- Request body: None
- Response: `List<Student>`

### `GET /api/students/public-id/{publicId}`
- Controller: `StudentController.java` / method `getByPublicId`
- Path params: publicId: UUID
- Query params: None
- Request body: None
- Response: `Student`

### `GET /api/students/status/{status}`
- Controller: `StudentController.java` / method `getByStatus`
- Path params: status: String
- Query params: None
- Request body: None
- Response: `List<Student>`

### `DELETE /api/students/{id}`
- Controller: `StudentController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Void`

### `GET /api/students/{id}`
- Controller: `StudentController.java` / method `getById`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Student`

### `PUT /api/students/{id}`
- Controller: `StudentController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Student`
- Response: `Student`

### `GET /api/tenants`
- Controller: `TenantController.java` / method `getAllTenants`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<TenantResponse>`

### `POST /api/tenants`
- Controller: `TenantController.java` / method `createTenant`
- Path params: None
- Query params: None
- Request body: `TenantRequest`
- Response: `TenantResponse`

### `GET /api/tenants/{id}`
- Controller: `TenantController.java` / method `getTenant`
- Path params: id: UUID
- Query params: None
- Request body: None
- Response: `TenantResponse`

### `PUT /api/tenants/{id}`
- Controller: `TenantController.java` / method `updateTenant`
- Path params: id: UUID
- Query params: None
- Request body: None
- Response: `TenantResponse`

### `PUT /api/tenants/{id}/disable`
- Controller: `TenantController.java` / method `disableTenant`
- Path params: id: UUID
- Query params: None
- Request body: None
- Response: `TenantResponse`

### `PUT /api/tenants/{id}/enable`
- Controller: `TenantController.java` / method `enableTenant`
- Path params: id: UUID
- Query params: None
- Request body: None
- Response: `TenantResponse`

### `GET /api/timetable`
- Controller: `TimetableController.java` / method `getAll`
- Path params: None
- Query params: None
- Request body: None
- Response: `List<Map<String, Object>>`

### `POST /api/timetable`
- Controller: `TimetableController.java` / method `create`
- Path params: None
- Query params: None
- Request body: `Map<String, Object>`
- Response: `Map<String, Object>`

### `DELETE /api/timetable/{id}`
- Controller: `TimetableController.java` / method `delete`
- Path params: id: Long
- Query params: None
- Request body: None
- Response: `Map<String, String>`

### `PUT /api/timetable/{id}`
- Controller: `TimetableController.java` / method `update`
- Path params: id: Long
- Query params: None
- Request body: `Map<String, Object>`
- Response: `Map<String, Object>`
