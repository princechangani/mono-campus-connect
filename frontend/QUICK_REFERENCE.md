# Frontend API Implementation - Quick Navigation

## 📚 Documentation Files

### Main Documentation
- **[API_INTEGRATION.md](./API_INTEGRATION.md)** - Complete API reference guide
  - All 24 API services documented
  - Method signatures and examples
  - React Query hooks guide
  - Type definitions reference

### Usage Patterns
- **[API_USAGE_EXAMPLES.md](./API_USAGE_EXAMPLES.md)** - Practical code examples
  - 10 different usage patterns
  - Form integration examples
  - Error handling patterns
  - Real-world component examples

### Implementation Summary
- **[FRONTEND_API_INTEGRATION_COMPLETE.md](../FRONTEND_API_INTEGRATION_COMPLETE.md)** - Overview of what's implemented
  - Feature checklist
  - File structure
  - Quick start examples
  - Endpoint summary

---

## 📂 Code Files

### Services (24 total)

#### Academic Management Services
```
src/lib/services/
├── academicYearService.ts      # Academic year management
├── programService.ts            # Program/degree programs
├── batchService.ts              # Student batches
└── programCourseService.ts       # Program-course mapping
```

#### User Management Services
```
src/lib/services/
├── studentService.ts            # Student records
├── facultyService.ts            # Faculty records
├── facultyEducationService.ts    # Faculty education details
└── facultyWorkExperienceService.ts # Faculty work history
```

#### Academic Operations Services
```
src/lib/services/
├── courseAssignmentService.ts    # Course assignments
├── attendanceSessionService.ts    # Attendance tracking
├── examScheduleService.ts         # Exam scheduling
└── markService.ts                 # Grade/mark management
```

#### Finance Services
```
src/lib/services/
├── feeStructureService.ts         # Fee structure definition
├── feeInvoiceService.ts           # Fee invoices
└── feePaymentService.ts           # Fee payments
```

#### Administrative Services
```
src/lib/services/
├── announcementService.ts         # Announcements
├── holidayService.ts              # Holiday calendar
├── leaveApplicationService.ts      # Leave applications
├── permissionService.ts            # Permission management
├── rolePermissionService.ts        # Role-permission mapping
└── roomService.ts                  # Room management
```

#### Document Services (MongoDB)
```
src/lib/services/
├── auditLogDocumentService.ts      # Audit logs
├── chatMessageDocumentService.ts    # Chat messages
└── reportSnapshotDocumentService.ts # Report snapshots
```

#### Supporting Files
```
src/lib/services/
└── index.ts                        # Export all services
```

### Hooks
```
src/hooks/
└── useApi.ts                       # React Query custom hooks
  - usePrograms, useCreateProgram, useUpdateProgram, useDeleteProgram
  - useAcademicYears, useCreateAcademicYear
  - useBatches, useCreateBatch
  - useStudents, useCreateStudent
  - useFaculty, useCreateFaculty
  - And more...
```

### Types
```
src/types/
└── models.ts                       # 30+ TypeScript interfaces
  - AcademicYear, Program, Batch
  - Student, Faculty
  - Announcement, Holiday
  - And more...
```

### API Client Factory
```
src/lib/
└── apiClient.ts                    # Organized API client
  - academicApi
  - userApi
  - courseApi
  - financeApi
  - adminApi
  - documentApi
```

---

## 🚀 Quick Start

### 1. Import and Use Service
```typescript
import { programService } from "@/lib/services";

// Use it
const programs = await programService.getAll();
```

### 2. Use React Hook (Recommended)
```typescript
import { usePrograms } from "@/hooks/useApi";

function MyComponent() {
  const { data, isLoading } = usePrograms();
  return <div>{isLoading ? 'Loading...' : data?.map(p => p.name)}</div>;
}
```

### 3. Use API Client Factory
```typescript
import apiClient from "@/lib/apiClient";

const programs = await apiClient.academic.programs.getAll();
const students = await apiClient.users.students.getAll();
```

---

## 📋 API Services by Category

### Academic APIs
- `/academic-years` - Academic year management
- `/programs` - Degree programs
- `/batches` - Student batches
- `/program-courses` - Program course mapping

### User APIs
- `/students` - Student management
- `/faculty` - Faculty management
- `/faculty-education` - Faculty education records
- `/faculty-work-experience` - Faculty work experience

### Course APIs
- `/course-assignments` - Course assignment to faculty
- `/attendance-sessions` - Attendance sessions
- `/exam-schedules` - Exam scheduling

### Finance APIs
- `/fee-structures` - Fee structure definition
- `/fee-invoices` - Fee invoices
- `/fee-payments` - Fee payments

### Admin APIs
- `/announcements` - System announcements
- `/holidays` - Holiday calendar
- `/leave-applications` - Leave applications
- `/permissions` - Permission management
- `/role-permissions` - Role-permission mapping
- `/rooms` - Room management

### Document APIs
- `/documents/audit-logs` - Audit logs
- `/documents/chat-messages` - Chat messages
- `/documents/report-snapshots` - Report snapshots

---

## 🔍 Finding What You Need

### Need to fetch all students?
```typescript
// Option 1: Service directly
const response = await studentService.getAll();

// Option 2: Hook (recommended for React)
const { data: students } = useStudents();

// Option 3: API client
const response = await apiClient.users.students.getAll();
```

### Need to create a new program?
```typescript
// Option 1: Service
await programService.create({ name: "...", code: "..." });

// Option 2: Hook (recommended)
const createProgram = useCreateProgram();
await createProgram.mutateAsync({ name: "...", code: "..." });

// Option 3: API client
await apiClient.academic.programs.create({ ... });
```

### Need to update a student?
```typescript
// Use the service directly
await studentService.update(studentId, { status: "active" });

// Or use hook
const updateStudent = useUpdateStudent();
await updateStudent.mutateAsync({ id: studentId, data: { status: "active" } });

// Or use API client
await apiClient.users.students.update(id, data);
```

---

## 📖 Type Definitions

All types are in `src/types/models.ts`:

```typescript
import type {
  Program,
  Student,
  Faculty,
  Announcement,
  Holiday,
  // ... and 25 more
} from "@/types/models";
```

---

## ✨ Features

✅ **24 API Services** - All major entities covered
✅ **Type Safe** - Full TypeScript support
✅ **React Query** - Built-in caching and synchronization
✅ **Custom Hooks** - Easy component integration
✅ **Error Handling** - Automatic toast notifications
✅ **JWT Auth** - Token injection built-in
✅ **Organized** - API client factory for clean code
✅ **Documented** - Comprehensive guides and examples
✅ **Production Ready** - Error handling and best practices

---

## 🎯 Next Steps

1. ✅ Read `API_INTEGRATION.md` to understand all available services
2. ✅ Check `API_USAGE_EXAMPLES.md` for code patterns
3. ✅ Start using hooks in your React components
4. ✅ Create pages and components using the services
5. ✅ Add form validation with Zod and react-hook-form
6. ✅ Handle loading and error states
7. ✅ Deploy with confidence!

---

## 💡 Pro Tips

1. **Always use hooks in React components** - Better performance and state management
2. **Check console for debug logs** - API calls log request/response in dev mode
3. **Handle errors gracefully** - Show user-friendly messages
4. **Use TypeScript interfaces** - Catch errors at compile time
5. **Test with Postman/Insomnia** - Test backend APIs directly first
6. **Read the docs** - All methods are well documented in service files

---

## 🆘 Troubleshooting

### API returns 401?
- Check your JWT token in localStorage
- Verify authentication endpoint
- Check browser console for error details

### Hook returns undefined?
- Check if query is enabled
- Verify API endpoint is correct
- Check network tab in DevTools

### TypeScript errors?
- Import types from `@/types/models`
- Check service return types
- Use proper type annotations

### Need more details?
- Check the service file directly
- Look at hook implementation
- Read the usage examples

---

## 📞 Support Resources

1. **API_INTEGRATION.md** - Detailed API reference
2. **API_USAGE_EXAMPLES.md** - Code examples
3. **Service files** - Source of truth for methods
4. **types/models.ts** - Data structure reference
5. **hooks/useApi.ts** - Hook implementations

---

**Everything you need is in place. Time to build! 🚀**

