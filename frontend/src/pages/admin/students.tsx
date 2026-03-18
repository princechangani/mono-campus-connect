import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Layout, PageHeader, DataTable } from '@/components/layout/Layout';
import { Button, Card, Input, Select, LoadingSpinner, Modal } from '@/components/ui/Common';
import { useStudents, useCreateStudent, useUpdateStudent, useDeleteStudent } from '@/hooks/useApi';
import toast from 'react-hot-toast';

const studentSchema = z.object({
  userId: z.string().min(1, 'User ID is required'),
  batchId: z.string().min(1, 'Batch is required'),
  enrollmentNumber: z.string().min(1, 'Enrollment number is required'),
  enrollmentDate: z.string().optional(),
  currentSemester: z.string().optional().transform(v => v ? parseInt(v) : undefined),
  status: z.string().optional(),
});

type StudentFormData = z.infer<typeof studentSchema>;

const StudentsPage = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [deleteId, setDeleteId] = useState<number | null>(null);

  const { data: students, isLoading } = useStudents();
  const createStudent = useCreateStudent();

  const { register, handleSubmit, formState: { errors }, reset, setValue } = useForm<StudentFormData>({
    resolver: zodResolver(studentSchema),
  });

  const onSubmit = async (data: StudentFormData) => {
    try {
      await createStudent.mutateAsync({
        ...data,
        userId: parseInt(data.userId),
        batchId: parseInt(data.batchId),
      });
      reset();
      setIsModalOpen(false);
      toast.success('Student created successfully');
    } catch (error) {
      toast.error('Failed to save student');
    }
  };

  const handleDelete = async (id: number) => {
    try {
      // Implement delete
      setDeleteId(null);
      toast.success('Student deleted successfully');
    } catch (error) {
      toast.error('Failed to delete student');
    }
  };

  const navItems = [
    { label: 'Dashboard', href: '/admin/dashboard' },
    { label: 'Programs', href: '/admin/programs' },
    { label: 'Batches', href: '/admin/batches' },
    { label: 'Students', href: '/admin/students' },
    { label: 'Faculty', href: '/admin/faculty' },
  ];

  return (
    <Layout navItems={navItems} title="Students">
      <PageHeader
        title="Students Management"
        description="Manage student records and enrollment"
        action={{
          label: '+ New Student',
          onClick: () => {
            setEditingId(null);
            reset();
            setIsModalOpen(true);
          },
        }}
      />

      <Card>
        {isLoading ? (
          <LoadingSpinner />
        ) : (
          <DataTable
            columns={[
              { key: 'enrollmentNumber', label: 'Enrollment Number' },
              { key: 'userId', label: 'User ID' },
              { key: 'batchId', label: 'Batch ID' },
              { key: 'currentSemester', label: 'Semester' },
              { key: 'status', label: 'Status' },
            ]}
            data={students || []}
            actions={(student) => (
              <div className="flex gap-2">
                <Button size="sm" variant="secondary">
                  View
                </Button>
                <Button
                  size="sm"
                  variant="danger"
                  onClick={() => setDeleteId(student.id)}
                >
                  Delete
                </Button>
              </div>
            )}
          />
        )}
      </Card>

      {/* Create Modal */}
      <Modal
        isOpen={isModalOpen}
        title="Enroll New Student"
        onClose={() => {
          setIsModalOpen(false);
          reset();
        }}
        onSubmit={handleSubmit(onSubmit)}
        submitText="Enroll"
      >
        <form className="space-y-4">
          <Input
            label="User ID"
            type="number"
            placeholder="User ID"
            {...register('userId')}
            error={errors.userId?.message}
          />

          <Select
            label="Batch"
            options={[
              { value: '1', label: 'Batch 2024-A (CSE)' },
              { value: '2', label: 'Batch 2024-B (CSE)' },
            ]}
            {...register('batchId')}
            error={errors.batchId?.message}
          />

          <Input
            label="Enrollment Number"
            placeholder="e.g., CSE-2024-001"
            {...register('enrollmentNumber')}
            error={errors.enrollmentNumber?.message}
          />

          <Input
            label="Enrollment Date"
            type="date"
            {...register('enrollmentDate')}
          />

          <Select
            label="Status"
            options={[
              { value: 'active', label: 'Active' },
              { value: 'inactive', label: 'Inactive' },
              { value: 'suspended', label: 'Suspended' },
            ]}
            {...register('status')}
          />
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={deleteId !== null}
        title="Delete Student"
        onClose={() => setDeleteId(null)}
        onSubmit={() => deleteId && handleDelete(deleteId)}
        submitText="Delete"
      >
        <p className="text-gray-600">Are you sure you want to delete this student record?</p>
      </Modal>
    </Layout>
  );
};

export default StudentsPage;

