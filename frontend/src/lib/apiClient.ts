/**
 * API Client Factory
 *
 * Central point for all API service usage
 * Provides easy access to all services with consistent interface
 */

import * as services from "@/lib/services";

// ============================================================================
// Academic Management APIs
// ============================================================================

export const academicApi = {
  years: services.academicYearService,
  programs: services.programService,
  batches: services.batchService,
  programCourses: services.programCourseService,
};

// ============================================================================
// User Management APIs
// ============================================================================

export const userApi = {
  students: services.studentService,
  faculty: services.facultyService,
  facultyEducation: services.facultyEducationService,
  facultyWorkExperience: services.facultyWorkExperienceService,
};

// ============================================================================
// Course Management APIs
// ============================================================================

export const courseApi = {
  assignments: services.courseAssignmentService,
};

// ============================================================================
// Attendance & Marks APIs
// ============================================================================

export const academicsApi = {
  attendance: services.attendanceSessionService,
  marks: services.markService,
};

// ============================================================================
// Exam APIs
// ============================================================================

export const examApi = {
  schedules: services.examScheduleService,
};

// ============================================================================
// Finance APIs
// ============================================================================

export const financeApi = {
  feeStructures: services.feeStructureService,
  invoices: services.feeInvoiceService,
  payments: services.feePaymentService,
};

// ============================================================================
// Administrative APIs
// ============================================================================

export const adminApi = {
  announcements: services.announcementService,
  holidays: services.holidayService,
  leaveApplications: services.leaveApplicationService,
  permissions: services.permissionService,
  rolePermissions: services.rolePermissionService,
  rooms: services.roomService,
};

// ============================================================================
// Document APIs (MongoDB)
// ============================================================================

export const documentApi = {
  auditLogs: services.auditLogDocumentService,
  chatMessages: services.chatMessageDocumentService,
  reportSnapshots: services.reportSnapshotDocumentService,
};

// ============================================================================
// Unified API Client
// ============================================================================

export const apiClient = {
  academic: academicApi,
  users: userApi,
  courses: courseApi,
  academics: academicsApi,
  exams: examApi,
  finance: financeApi,
  admin: adminApi,
  documents: documentApi,
};

// Type exports for easier usage
export type ApiClient = typeof apiClient;

export default apiClient;

