// ============================================================================
// Academic Management Types
// ============================================================================

export interface AcademicYear {
  id?: number;
  academicYearPublicId?: string;
  label: string;
  startDate?: string;
  endDate?: string;
  isCurrent?: boolean;
  tenantId?: string;
}

export interface Program {
  id?: number;
  programPublicId?: string;
  name: string;
  code: string;
  departmentId: number;
  degreeLevel?: string;
  durationYears?: number;
  totalSemesters?: number;
  totalCredits?: number;
  isActive?: boolean;
  tenantId?: string;
}

export interface Batch {
  id?: number;
  batchPublicId?: string;
  name: string;
  programId: number;
  academicYearId?: number;
  currentSemester?: number;
  maxStudents?: number;
  classTeacherId?: number;
  tenantId?: string;
}

export interface ProgramCourse {
  id?: number;
  programId: number;
  courseId: number;
  semesterNumber?: number;
  isElective?: boolean;
  isMandatory?: boolean;
}

// ============================================================================
// User Management Types
// ============================================================================

export interface Student {
  id?: number;
  studentPublicId?: string;
  userId: number;
  batchId: number;
  enrollmentNumber: string;
  enrollmentDate?: string;
  currentSemester?: number;
  status?: string;
  tenantId?: string;
}

export interface Faculty {
  id?: number;
  facultyPublicId?: string;
  userId: number;
  departmentId: number;
  designation: string;
  employeeId: string;
  employmentType?: string;
  tenantId?: string;
}

export interface FacultyEducation {
  id?: number;
  facultyId: number;
  degree: string;
  fieldOfStudy: string;
  institution: string;
  passingYear?: string;
  gradeOrPercentage?: string;
}

export interface FacultyWorkExperience {
  id?: number;
  facultyId: number;
  organization: string;
  role: string;
  fromDate?: string;
  toDate?: string;
  description?: string;
}

// ============================================================================
// Attendance & Marks Types
// ============================================================================

export interface AttendanceSession {
  id?: number;
  courseAssignmentId: number;
  slotId?: number;
  roomId?: number;
  sessionDate: string;
  topicCovered?: string;
  conductedBy?: number;
}

export interface Mark {
  id?: number;
  studentId: number;
  examId: number;
  marksObtained?: number;
}

// ============================================================================
// Course Management Types
// ============================================================================

export interface CourseAssignment {
  id?: number;
  courseId: number;
  facultyId: number;
  batchId: number;
  academicYearId?: number;
  semesterNumber?: number;
  section?: string;
}

// ============================================================================
// Exam Types
// ============================================================================

export interface ExamSchedule {
  id?: number;
  examId: number;
  courseId?: number;
  roomId?: number;
  examDate: string;
  startTime?: string;
  endTime?: string;
  invigilatorId?: number;
}

// ============================================================================
// Fee Management Types
// ============================================================================

export interface FeeStructure {
  id?: number;
  programId: number;
  semesterNumber?: number;
  academicYearId?: number;
  feeName: string;
  totalAmount: number;
  tenantId?: string;
}

export interface FeeInvoice {
  id?: number;
  studentId: number;
  feeStructureId?: number;
  issueDate?: string;
  dueDate?: string;
  status?: string;
}

export interface FeePayment {
  id?: number;
  studentId: number;
  amountPaid?: number;
  paymentDate?: string;
  paymentMode?: string;
  status?: string;
  transactionReference?: string;
}

// ============================================================================
// Administrative Types
// ============================================================================

export interface Announcement {
  id?: number;
  announcementPublicId?: string;
  title: string;
  content: string;
  audience?: string;
  priority?: string;
  pinnedUntil?: string;
  expiresAt?: string;
  publishedAt?: string;
  tenantId?: string;
}

export interface Holiday {
  id?: number;
  holidayPublicId?: string;
  title: string;
  holidayDate: string;
  holidayType: string;
  appliesTo?: string;
  description?: string;
  tenantId?: string;
}

export interface LeaveApplication {
  id?: number;
  leavePublicId?: string;
  leaveType: string;
  fromDate: string;
  toDate: string;
  reason: string;
  documentUrl?: string;
  status?: string;
  reviewedBy?: number;
  reviewerRemarks?: string;
  reviewedAt?: string;
  applicantUserId?: number;
}

export interface Permission {
  id?: number;
  module: string;
  action: string;
  resource?: string;
}

export interface RolePermission {
  id?: number;
  roleId: number;
  permissionId: number;
}

export interface Room {
  id?: number;
  roomPublicId?: string;
  building: string;
  floor?: string;
  roomNumber: string;
  name: string;
  capacity: number;
  roomType: string;
}

// ============================================================================
// MongoDB Document Types
// ============================================================================

export interface AuditLogDocument {
  id?: string;
  tenantId: string;
  actorUserId?: number;
  actorRole?: string;
  action: string;
  resourceType: string;
  resourceId?: string;
  changes?: Record<string, any>;
  createdAt?: string;
}

export interface ChatMessageDocument {
  id?: string;
  publicId?: string;
  tenantId: string;
  channelId: string;
  senderId: number;
  text: string;
  attachments?: string[];
  reactions?: Record<string, any>;
  readBy?: number[];
  messageType?: string;
  replyToMessageId?: string;
  createdAt?: string;
  editedAt?: string;
  updatedAt?: string;
  updatedBy?: number;
}

export interface ReportSnapshotDocument {
  id?: string;
  publicId?: string;
  tenantId: string;
  reportType: string;
  referenceType?: string;
  referenceId?: string;
  data?: Record<string, any>;
  generatedAt?: string;
  expiresAt?: string;
}

// ============================================================================
// API Response Types
// ============================================================================

export interface ApiResponse<T> {
  data: T;
  status: number;
  statusText: string;
}

export interface ApiError {
  message: string;
  status?: number;
  data?: Record<string, any>;
}

// ============================================================================
// Pagination Types
// ============================================================================

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}

