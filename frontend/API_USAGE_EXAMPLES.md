/**
 * API Usage Examples
 * 
 * This file demonstrates various ways to use the API services
 * in your React components
 */

// ============================================================================
// PATTERN 1: Using Service Directly
// ============================================================================

import { programService } from "@/lib/services";

// Simple fetch without hooks
async function fetchPrograms() {
  try {
    const response = await programService.getAll();
    console.log(response.data);
  } catch (error) {
    console.error("Failed to fetch programs", error);
  }
}

// ============================================================================
// PATTERN 2: Using React Query Hooks (RECOMMENDED)
// ============================================================================

import {
  usePrograms,
  useCreateProgram,
  useUpdateProgram,
  useDeleteProgram,
} from "@/hooks/useApi";

function ProgramsManagement() {
  const { data: programs, isLoading, error } = usePrograms();
  const createMutation = useCreateProgram();
  const updateMutation = useUpdateProgram();
  const deleteMutation = useDeleteProgram();

  return (
    <div>
      {isLoading && <p>Loading programs...</p>}
      {error && <p>Error: {error.message}</p>}
      {programs?.map((program) => (
        <div key={program.id}>
          <h3>{program.name}</h3>
          <button onClick={() => updateMutation.mutateAsync({ id: program.id, data: { name: "Updated" } })}>
            Update
          </button>
          <button onClick={() => deleteMutation.mutateAsync(program.id)}>
            Delete
          </button>
        </div>
      ))}
      <button onClick={() => createMutation.mutateAsync({ name: "New Program", code: "NP001", departmentId: 1 })}>
        Create Program
      </button>
    </div>
  );
}

// ============================================================================
// PATTERN 3: Using API Client Factory
// ============================================================================

import apiClient from "@/lib/apiClient";

async function academicOperations() {
  // All services organized by category
  const years = await apiClient.academic.years.getAll();
  const programs = await apiClient.academic.programs.getAll();
  const batches = await apiClient.academic.batches.getAll();

  const students = await apiClient.users.students.getAll();
  const faculty = await apiClient.users.faculty.getAll();

  const announcements = await apiClient.admin.announcements.getAll();
  const holidays = await apiClient.admin.holidays.getAll();

  return { years, programs, batches, students, faculty, announcements, holidays };
}

// ============================================================================
// PATTERN 4: Fetching Related Data
// ============================================================================

import { useProgramsByDepartment, useBatchesByProgram, useStudentsByBatch } from "@/hooks/useApi";

function DepartmentHierarchy({ departmentId }: { departmentId: number }) {
  const { data: programs } = useProgramsByDepartment(departmentId);

  return (
    <div>
      {programs?.map((program) => (
        <ProgramSection key={program.id} programId={program.id} programName={program.name} />
      ))}
    </div>
  );
}

function ProgramSection({ programId, programName }: { programId: number; programName: string }) {
  const { data: batches } = useBatchesByProgram(programId);

  return (
    <div>
      <h2>{programName}</h2>
      {batches?.map((batch) => (
        <BatchSection key={batch.id} batchId={batch.id} batchName={batch.name} />
      ))}
    </div>
  );
}

function BatchSection({ batchId, batchName }: { batchId: number; batchName: string }) {
  const { data: students } = useStudentsByBatch(batchId);

  return (
    <div>
      <h3>{batchName}</h3>
      <ul>
        {students?.map((student) => (
          <li key={student.id}>{student.enrollmentNumber}</li>
        ))}
      </ul>
    </div>
  );
}

// ============================================================================
// PATTERN 5: Form Integration with React Hook Form
// ============================================================================

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";

const programSchema = z.object({
  name: z.string().min(1, "Program name is required"),
  code: z.string().min(1, "Program code is required"),
  departmentId: z.number().min(1),
  degreeLevel: z.string().optional(),
  durationYears: z.number().optional(),
});

type ProgramFormData = z.infer<typeof programSchema>;

function CreateProgramForm() {
  const { register, handleSubmit, formState: { errors } } = useForm<ProgramFormData>({
    resolver: zodResolver(programSchema),
  });
  const createProgram = useCreateProgram();

  const onSubmit = async (data: ProgramFormData) => {
    await createProgram.mutateAsync(data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <input {...register("name")} placeholder="Program Name" />
      {errors.name && <span>{errors.name.message}</span>}

      <input {...register("code")} placeholder="Program Code" />
      {errors.code && <span>{errors.code.message}</span>}

      <input {...register("departmentId", { valueAsNumber: true })} placeholder="Department ID" type="number" />
      {errors.departmentId && <span>{errors.departmentId.message}</span>}

      <button type="submit" disabled={createProgram.isLoading}>
        {createProgram.isLoading ? "Creating..." : "Create Program"}
      </button>
    </form>
  );
}

// ============================================================================
// PATTERN 6: Infinite Queries (for pagination)
// ============================================================================

import { useInfiniteQuery } from "react-query";

function InfiniteStudentsList() {
  const { data, fetchNextPage, hasNextPage, isFetchingNextPage } = useInfiniteQuery(
    "students",
    async ({ pageParam = 1 }) => {
      const response = await apiClient.users.students.getAll();
      return response.data;
    },
    {
      getNextPageParam: (lastPage, pages) => pages.length + 1,
    }
  );

  return (
    <div>
      {data?.pages.map((page) =>
        page.map((student) => <div key={student.id}>{student.enrollmentNumber}</div>)
      )}
      {hasNextPage && (
        <button onClick={() => fetchNextPage()} disabled={isFetchingNextPage}>
          {isFetchingNextPage ? "Loading more..." : "Load More"}
        </button>
      )}
    </div>
  );
}

// ============================================================================
// PATTERN 7: Error Handling
// ============================================================================

import toast from "react-hot-toast";

function SafeDataFetching() {
  const { data, error, isError } = usePrograms();

  if (isError) {
    toast.error(
      error?.response?.data?.message || "Failed to fetch programs"
    );
  }

  return (
    <div>
      {data?.map((program) => (
        <div key={program.id}>{program.name}</div>
      ))}
    </div>
  );
}

// ============================================================================
// PATTERN 8: Mutation with Optimistic Updates
// ============================================================================

function OptimisticUpdate() {
  const queryClient = useQueryClient();
  const updateMutation = useUpdateProgram();

  const handleUpdate = async (id: number, newData: any) => {
    // Optimistic update
    queryClient.setQueryData(["program", id], newData);

    try {
      await updateMutation.mutateAsync({ id, data: newData });
    } catch (error) {
      // Revert on error
      queryClient.invalidateQueries(["program", id]);
    }
  };

  return <div>Content with optimistic updates</div>;
}

// ============================================================================
// PATTERN 9: Dependent Queries
// ============================================================================

function DependentQueries({ departmentId }: { departmentId: number }) {
  // First query
  const { data: departments } = useQuery("departments", () =>
    apiClient.academic.programs.getByDepartmentId(departmentId)
  );

  // Second query depends on first
  const { data: batches } = useQuery(
    ["batches", departments?.id],
    () => apiClient.academic.batches.getByProgramId(departments?.id),
    { enabled: !!departments } // Only run if departments exist
  );

  return (
    <div>
      {/* Render based on data */}
    </div>
  );
}

// ============================================================================
// PATTERN 10: Batch Operations
// ============================================================================

async function batchOperations() {
  const createBatch = useCreateBatch();
  const updateBatch = useUpdateBatch();

  // Create multiple items
  const items = [
    { name: "Batch 1", programId: 1 },
    { name: "Batch 2", programId: 1 },
    { name: "Batch 3", programId: 2 },
  ];

  try {
    const results = await Promise.all(
      items.map((item) => createBatch.mutateAsync(item))
    );
    toast.success(`Created ${results.length} batches`);
  } catch (error) {
    toast.error("Failed to create batches");
  }
}

export {
  fetchPrograms,
  ProgramsManagement,
  academicOperations,
  DepartmentHierarchy,
  CreateProgramForm,
  InfiniteStudentsList,
  SafeDataFetching,
  OptimisticUpdate,
  DependentQueries,
  batchOperations,
};

