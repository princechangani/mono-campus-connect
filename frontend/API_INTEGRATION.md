# API Integration Documentation

## Overview

This document outlines all the API services implemented in the frontend for the Mono Campus Connect application. All services are built using Axios and integrated with React Query for state management.

## Directory Structure

```
src/
├── lib/
│   ├── services/          # API service layer
│   │   ├── academicYearService.ts
│   │   ├── programService.ts
│   │   ├── batchService.ts
│   │   ├── studentService.ts
│   │   ├── facultyService.ts
│   │   ├── facultyEducationService.ts
│   │   ├── facultyWorkExperienceService.ts
│   │   ├── courseAssignmentService.ts
│   │   ├── attendanceSessionService.ts
│   │   ├── examScheduleService.ts
│   │   ├── markService.ts
│   │   ├── feeStructureService.ts
│   │   ├── feeInvoiceService.ts
│   │   ├── feePaymentService.ts
│   │   ├── announcementService.ts
│   │   ├── holidayService.ts
│   │   ├── leaveApplicationService.ts
│   │   ├── permissionService.ts
│   │   ├── rolePermissionService.ts
│   │   ├── roomService.ts
│   │   ├── auditLogDocumentService.ts
│   │   ├── chatMessageDocumentService.ts
│   │   ├── reportSnapshotDocumentService.ts
│   │   └── index.ts                    # Export all services
│   ├── api.ts                          # Axios instance with interceptors
│   └── auth.ts                         # Authentication utilities
├── hooks/
│   └── useApi.ts                       # React Query custom hooks
└── types/
    └── models.ts                       # TypeScript interfaces for all entities
```

## API Services

### Academic Management Services

#### Academic Year Service
```typescript
import { academicYearService } from "@/lib/services";

// Create
academicYearService.create(data)

// Read
academicYearService.getAll()
academicYearService.getById(id)
academicYearService.getCurrentAcademicYear()
academicYearService.getByLabel(label)

// Update
academicYearService.update(id, data)

// Delete
academicYearService.delete(id)
```

#### Program Service
```typescript
import { programService } from "@/lib/services";

// Create
programService.create(data)

// Read
programService.getAll()
programService.getById(id)
programService.getByPublicId(publicId)
programService.getByDepartmentId(departmentId)

// Update
programService.update(id, data)

// Delete
programService.delete(id)
```

#### Batch Service
```typescript
import { batchService } from "@/lib/services";

// Create
batchService.create(data)

// Read
batchService.getAll()
batchService.getById(id)
batchService.getByPublicId(publicId)
batchService.getByProgramId(programId)

// Update
batchService.update(id, data)

// Delete
batchService.delete(id)
```

#### Program Course Service
```typescript
import { programCourseService } from "@/lib/services";

// Create
programCourseService.create(data)

// Read
programCourseService.getAll()
programCourseService.getById(id)
programCourseService.getByProgramId(programId)
programCourseService.getByCourseId(courseId)

// Update
programCourseService.update(id, data)

// Delete
programCourseService.delete(id)
```

### User Management Services

#### Student Service
```typescript
import { studentService } from "@/lib/services";

// CRUD operations
studentService.create(data)
studentService.getAll()
studentService.getById(id)
studentService.getByPublicId(publicId)
studentService.getByBatchId(batchId)
studentService.getByStatus(status)
studentService.update(id, data)
studentService.delete(id)
```

#### Faculty Service
```typescript
import { facultyService } from "@/lib/services";

// CRUD operations
facultyService.create(data)
facultyService.getAll()
facultyService.getById(id)
facultyService.getByPublicId(publicId)
facultyService.getByDepartmentId(departmentId)
facultyService.update(id, data)
facultyService.delete(id)
```

#### Faculty Education Service
```typescript
import { facultyEducationService } from "@/lib/services";

// CRUD operations
facultyEducationService.create(data)
facultyEducationService.getAll()
facultyEducationService.getById(id)
facultyEducationService.getByFacultyId(facultyId)
facultyEducationService.update(id, data)
facultyEducationService.delete(id)
```

#### Faculty Work Experience Service
```typescript
import { facultyWorkExperienceService } from "@/lib/services";

// CRUD operations
facultyWorkExperienceService.create(data)
facultyWorkExperienceService.getAll()
facultyWorkExperienceService.getById(id)
facultyWorkExperienceService.getByFacultyId(facultyId)
facultyWorkExperienceService.update(id, data)
facultyWorkExperienceService.delete(id)
```

### Course Management Services

#### Course Assignment Service
```typescript
import { courseAssignmentService } from "@/lib/services";

// CRUD operations
courseAssignmentService.create(data)
courseAssignmentService.getAll()
courseAssignmentService.getById(id)
courseAssignmentService.getByFacultyId(facultyId)
courseAssignmentService.getByCourseId(courseId)
courseAssignmentService.update(id, data)
courseAssignmentService.delete(id)
```

### Attendance & Marks Services

#### Attendance Session Service
```typescript
import { attendanceSessionService } from "@/lib/services";

// CRUD operations
attendanceSessionService.create(data)
attendanceSessionService.getAll()
attendanceSessionService.getById(id)
attendanceSessionService.getByCourseAssignmentId(courseAssignmentId)
attendanceSessionService.getByConductedBy(facultyId)
attendanceSessionService.update(id, data)
attendanceSessionService.delete(id)
```

#### Mark Service
```typescript
import { markService } from "@/lib/services";

// CRUD operations
markService.create(data)
markService.getAll()
markService.getById(id)
markService.getByStudentId(studentId)
markService.getByExamId(examId)
markService.update(id, data)
markService.delete(id)
```

### Exam Services

#### Exam Schedule Service
```typescript
import { examScheduleService } from "@/lib/services";

// CRUD operations
examScheduleService.create(data)
examScheduleService.getAll()
examScheduleService.getById(id)
examScheduleService.getByExamId(examId)
examScheduleService.getByRoomId(roomId)
examScheduleService.update(id, data)
examScheduleService.delete(id)
```

### Fee Management Services

#### Fee Structure Service
```typescript
import { feeStructureService } from "@/lib/services";

// CRUD operations
feeStructureService.create(data)
feeStructureService.getAll()
feeStructureService.getById(id)
feeStructureService.getByProgramId(programId)
feeStructureService.getByTenantId(tenantId)
feeStructureService.update(id, data)
feeStructureService.delete(id)
```

#### Fee Invoice Service
```typescript
import { feeInvoiceService } from "@/lib/services";

// CRUD operations
feeInvoiceService.create(data)
feeInvoiceService.getAll()
feeInvoiceService.getById(id)
feeInvoiceService.getByStudentId(studentId)
feeInvoiceService.getByStatus(status)
feeInvoiceService.update(id, data)
feeInvoiceService.delete(id)
```

#### Fee Payment Service
```typescript
import { feePaymentService } from "@/lib/services";

// CRUD operations
feePaymentService.create(data)
feePaymentService.getAll()
feePaymentService.getById(id)
feePaymentService.getByStudentId(studentId)
feePaymentService.getByStatus(status)
feePaymentService.update(id, data)
feePaymentService.delete(id)
```

### Administrative Services

#### Announcement Service
```typescript
import { announcementService } from "@/lib/services";

// CRUD operations
announcementService.create(data)
announcementService.getAll()
announcementService.getById(id)
announcementService.getActive()
announcementService.getByTenantId(tenantId)
announcementService.update(id, data)
announcementService.delete(id)
```

#### Holiday Service
```typescript
import { holidayService } from "@/lib/services";

// CRUD operations
holidayService.create(data)
holidayService.getAll()
holidayService.getById(id)
holidayService.getByTenantId(tenantId)
holidayService.update(id, data)
holidayService.delete(id)
```

#### Leave Application Service
```typescript
import { leaveApplicationService } from "@/lib/services";

// CRUD operations
leaveApplicationService.create(data)
leaveApplicationService.getAll()
leaveApplicationService.getById(id)
leaveApplicationService.getByApplicantUserId(userId)
leaveApplicationService.getByApprovalStatus(status)
leaveApplicationService.update(id, data)
leaveApplicationService.delete(id)
```

#### Permission Service
```typescript
import { permissionService } from "@/lib/services";

// CRUD operations
permissionService.create(data)
permissionService.getAll()
permissionService.getById(id)
permissionService.getByModuleAndAction(module, action)
permissionService.update(id, data)
permissionService.delete(id)
```

#### Role Permission Service
```typescript
import { rolePermissionService } from "@/lib/services";

// CRUD operations
rolePermissionService.create(data)
rolePermissionService.getAll()
rolePermissionService.getById(id)
rolePermissionService.getByRoleId(roleId)
rolePermissionService.getByPermissionId(permissionId)
rolePermissionService.update(id, data)
rolePermissionService.delete(id)
```

#### Room Service
```typescript
import { roomService } from "@/lib/services";

// CRUD operations
roomService.create(data)
roomService.getAll()
roomService.getById(id)
roomService.getByRoomNumber(roomNumber)
roomService.getByBuilding(building)
roomService.getByRoomType(roomType)
roomService.update(id, data)
roomService.delete(id)
```

### MongoDB Document Services

#### Audit Log Document Service
```typescript
import { auditLogDocumentService } from "@/lib/services";

// CRUD operations
auditLogDocumentService.create(data)
auditLogDocumentService.getAll()
auditLogDocumentService.getById(id)
auditLogDocumentService.getByTenantId(tenantId)
auditLogDocumentService.getByResourceType(resourceType)
auditLogDocumentService.update(id, data)
auditLogDocumentService.delete(id)
```

#### Chat Message Document Service
```typescript
import { chatMessageDocumentService } from "@/lib/services";

// CRUD operations
chatMessageDocumentService.create(data)
chatMessageDocumentService.getAll()
chatMessageDocumentService.getById(id)
chatMessageDocumentService.getByPublicId(tenantId, publicId)
chatMessageDocumentService.getByChannelId(channelId)
chatMessageDocumentService.getBySenderId(senderId)
chatMessageDocumentService.getByTenantAndChannel(tenantId, channelId)
chatMessageDocumentService.update(id, data)
chatMessageDocumentService.delete(id)
```

#### Report Snapshot Document Service
```typescript
import { reportSnapshotDocumentService } from "@/lib/services";

// CRUD operations
reportSnapshotDocumentService.create(data)
reportSnapshotDocumentService.getAll()
reportSnapshotDocumentService.getById(id)
reportSnapshotDocumentService.getByPublicId(tenantId, publicId)
reportSnapshotDocumentService.getByTenantId(tenantId)
reportSnapshotDocumentService.getByReportType(reportType)
reportSnapshotDocumentService.update(id, data)
reportSnapshotDocumentService.delete(id)
```

## React Query Hooks

Custom hooks are available for easy integration with React components:

```typescript
import {
  usePrograms,
  useProgram,
  useCreateProgram,
  useUpdateProgram,
  useDeleteProgram,
  useProgramsByDepartment,
  useBatches,
  useBatch,
  useStudents,
  useStudent,
  useFaculty,
  useFacultyMember,
  // ... and more
} from "@/hooks/useApi";
```

### Example Usage

```typescript
function ProgramsPage() {
  const { data: programs, isLoading, error } = usePrograms();
  const createProgramMutation = useCreateProgram();

  const handleCreate = async (data) => {
    await createProgramMutation.mutateAsync(data);
  };

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <div>
      {programs?.map((program) => (
        <div key={program.id}>{program.name}</div>
      ))}
      <button onClick={() => handleCreate({ name: "New Program" })}>
        Create Program
      </button>
    </div>
  );
}
```

## Type Definitions

All types are defined in `src/types/models.ts` and can be imported as:

```typescript
import type {
  AcademicYear,
  Program,
  Batch,
  Student,
  Faculty,
  // ... and more
} from "@/types/models";
```

## Error Handling

All API calls are wrapped with error handling via React Query. Errors automatically show toast notifications:

```typescript
const mutation = useCreateProgram();

// Automatically shows error toast if request fails
await mutation.mutateAsync(data);
```

## Features

✅ Full CRUD operations for all entities
✅ Type-safe with TypeScript
✅ Automatic JWT token handling via interceptors
✅ Built-in error handling with toast notifications
✅ React Query integration for caching and synchronization
✅ Comprehensive type definitions
✅ Easy to extend and customize

## Base URL

The API base URL is set to `/api` in the axios instance. You can configure it via environment variables:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

Then update `src/lib/api.ts`:

```typescript
const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || "/api",
  // ...
});
```

