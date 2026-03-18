import { useQuery, useMutation, useQueryClient } from "react-query";
import toast from "react-hot-toast";
import * as services from "@/lib/services";

// ============================================================================
// Academic Year Hooks
// ============================================================================

export const useAcademicYears = () => {
  return useQuery("academicYears", () =>
    services.academicYearService.getAll().then((res) => res.data)
  );
};

export const useAcademicYear = (id: number) => {
  return useQuery(["academicYear", id], () =>
    services.academicYearService.getById(id).then((res) => res.data)
  );
};

export const useCreateAcademicYear = () => {
  const queryClient = useQueryClient();
  return useMutation(
    (data) => services.academicYearService.create(data).then((res) => res.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("academicYears");
        toast.success("Academic year created successfully");
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || "Failed to create academic year");
      },
    }
  );
};

export const useUpdateAcademicYear = () => {
  const queryClient = useQueryClient();
  return useMutation(
    ({ id, data }: { id: number; data: any }) =>
      services.academicYearService.update(id, data).then((res) => res.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("academicYears");
        toast.success("Academic year updated successfully");
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || "Failed to update academic year");
      },
    }
  );
};

export const useDeleteAcademicYear = () => {
  const queryClient = useQueryClient();
  return useMutation(
    (id: number) => services.academicYearService.delete(id),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("academicYears");
        toast.success("Academic year deleted successfully");
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || "Failed to delete academic year");
      },
    }
  );
};

// ============================================================================
// Program Hooks
// ============================================================================

export const usePrograms = () => {
  return useQuery("programs", () =>
    services.programService.getAll().then((res) => res.data)
  );
};

export const useProgram = (id: number) => {
  return useQuery(["program", id], () =>
    services.programService.getById(id).then((res) => res.data)
  );
};

export const useProgramsByDepartment = (departmentId: number) => {
  return useQuery(["programs", departmentId], () =>
    services.programService.getByDepartmentId(departmentId).then((res) => res.data)
  );
};

export const useCreateProgram = () => {
  const queryClient = useQueryClient();
  return useMutation(
    (data) => services.programService.create(data).then((res) => res.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("programs");
        toast.success("Program created successfully");
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || "Failed to create program");
      },
    }
  );
};

export const useUpdateProgram = () => {
  const queryClient = useQueryClient();
  return useMutation(
    ({ id, data }: { id: number; data: any }) =>
      services.programService.update(id, data).then((res) => res.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("programs");
        toast.success("Program updated successfully");
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || "Failed to update program");
      },
    }
  );
};

export const useDeleteProgram = () => {
  const queryClient = useQueryClient();
  return useMutation(
    (id: number) => services.programService.delete(id),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("programs");
        toast.success("Program deleted successfully");
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || "Failed to delete program");
      },
    }
  );
};

// ============================================================================
// Batch Hooks
// ============================================================================

export const useBatches = () => {
  return useQuery("batches", () =>
    services.batchService.getAll().then((res) => res.data)
  );
};

export const useBatch = (id: number) => {
  return useQuery(["batch", id], () =>
    services.batchService.getById(id).then((res) => res.data)
  );
};

export const useBatchesByProgram = (programId: number) => {
  return useQuery(["batches", programId], () =>
    services.batchService.getByProgramId(programId).then((res) => res.data)
  );
};

export const useCreateBatch = () => {
  const queryClient = useQueryClient();
  return useMutation(
    (data) => services.batchService.create(data).then((res) => res.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("batches");
        toast.success("Batch created successfully");
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || "Failed to create batch");
      },
    }
  );
};

export const useUpdateBatch = () => {
  const queryClient = useQueryClient();
  return useMutation(
    ({ id, data }: { id: number; data: any }) =>
      services.batchService.update(id, data).then((res) => res.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("batches");
        toast.success("Batch updated successfully");
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || "Failed to update batch");
      },
    }
  );
};

export const useDeleteBatch = () => {
  const queryClient = useQueryClient();
  return useMutation(
    (id: number) => services.batchService.delete(id),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("batches");
        toast.success("Batch deleted successfully");
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || "Failed to delete batch");
      },
    }
  );
};

// ============================================================================
// Student Hooks
// ============================================================================

export const useStudents = () => {
  return useQuery("students", () =>
    services.studentService.getAll().then((res) => res.data)
  );
};

export const useStudent = (id: number) => {
  return useQuery(["student", id], () =>
    services.studentService.getById(id).then((res) => res.data)
  );
};

export const useStudentsByBatch = (batchId: number) => {
  return useQuery(["students", batchId], () =>
    services.studentService.getByBatchId(batchId).then((res) => res.data)
  );
};

export const useCreateStudent = () => {
  const queryClient = useQueryClient();
  return useMutation(
    (data) => services.studentService.create(data).then((res) => res.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("students");
        toast.success("Student created successfully");
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || "Failed to create student");
      },
    }
  );
};

// ============================================================================
// Faculty Hooks
// ============================================================================

export const useFaculty = () => {
  return useQuery("faculty", () =>
    services.facultyService.getAll().then((res) => res.data)
  );
};

export const useFacultyMember = (id: number) => {
  return useQuery(["faculty", id], () =>
    services.facultyService.getById(id).then((res) => res.data)
  );
};

export const useFacultyByDepartment = (departmentId: number) => {
  return useQuery(["faculty", departmentId], () =>
    services.facultyService.getByDepartmentId(departmentId).then((res) => res.data)
  );
};

export const useCreateFaculty = () => {
  const queryClient = useQueryClient();
  return useMutation(
    (data) => services.facultyService.create(data).then((res) => res.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("faculty");
        toast.success("Faculty created successfully");
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || "Failed to create faculty");
      },
    }
  );
};

