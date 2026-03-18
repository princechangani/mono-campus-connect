import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Layout, PageHeader, DataTable } from '@/components/layout/Layout';
import { Button, Card, Input, Select, LoadingSpinner, Modal } from '@/components/ui/Common';
import { useFaculty, useCreateFaculty } from '@/hooks/useApi';
import toast from 'react-hot-toast';

const facultySchema = z.object({
  userId: z.string().min(1, 'User ID is required'),
  departmentId: z.string().min(1, 'Department is required'),
  designation: z.string().min(1, 'Designation is required'),
  employeeId: z.string().min(1, 'Employee ID is required'),
  employmentType: z.string().optional(),
});

type FacultyFormData = z.infer<typeof facultySchema>;

const FacultyPage = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [deleteId, setDeleteId] = useState<number | null>(null);

  const { data: facultyList, isLoading } = useFaculty();
  const createFaculty = useCreateFaculty();

  const { register, handleSubmit, formState: { errors }, reset } = useForm<FacultyFormData>({
    resolver: zodResolver(facultySchema),
  });

  const onSubmit = async (data: FacultyFormData) => {
    try {
      await createFaculty.mutateAsync({
        ...data,
        userId: parseInt(data.userId),
        departmentId: parseInt(data.departmentId),
      });
      reset();
      setIsModalOpen(false);
      toast.success('Faculty member added successfully');
    } catch (error) {
      toast.error('Failed to add faculty member');
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
    <Layout navItems={navItems} title="Faculty">
      <PageHeader
        title="Faculty Management"
        description="Manage faculty members and their assignments"
        action={{
          label: '+ Add Faculty',
          onClick: () => {
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
              { key: 'employeeId', label: 'Employee ID' },
              { key: 'userId', label: 'User ID' },
              { key: 'designation', label: 'Designation' },
              { key: 'departmentId', label: 'Department' },
              { key: 'employmentType', label: 'Employment Type' },
            ]}
            data={facultyList || []}
            actions={(faculty) => (
              <div className="flex gap-2">
                <Button size="sm" variant="secondary">
                  View Profile
                </Button>
                <Button
                  size="sm"
                  variant="danger"
                  onClick={() => setDeleteId(faculty.id)}
                >
                  Remove
                </Button>
              </div>
            )}
          />
        )}
      </Card>

      {/* Create Modal */}
      <Modal
        isOpen={isModalOpen}
        title="Add Faculty Member"
        onClose={() => {
          setIsModalOpen(false);
          reset();
        }}
        onSubmit={handleSubmit(onSubmit)}
        submitText="Add"
      >
        <form className="space-y-4">
          <Input
            label="User ID"
            type="number"
            placeholder="User ID"
            {...register('userId')}
            error={errors.userId?.message}
          />

          <Input
            label="Employee ID"
            placeholder="e.g., EMP-001"
            {...register('employeeId')}
            error={errors.employeeId?.message}
          />

          <Select
            label="Department"
            options={[
              { value: '1', label: 'Computer Science' },
              { value: '2', label: 'Engineering' },
              { value: '3', label: 'Arts' },
            ]}
            {...register('departmentId')}
            error={errors.departmentId?.message}
          />

          <Input
            label="Designation"
            placeholder="e.g., Associate Professor"
            {...register('designation')}
            error={errors.designation?.message}
          />

          <Select
            label="Employment Type"
            options={[
              { value: 'full-time', label: 'Full Time' },
              { value: 'part-time', label: 'Part Time' },
              { value: 'contract', label: 'Contract' },
            ]}
            {...register('employmentType')}
          />
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={deleteId !== null}
        title="Remove Faculty"
        onClose={() => setDeleteId(null)}
        onSubmit={() => {
          setDeleteId(null);
          toast.success('Faculty member removed successfully');
        }}
        submitText="Remove"
      >
        <p className="text-gray-600">Are you sure you want to remove this faculty member?</p>
      </Modal>
    </Layout>
  );
};

export default FacultyPage;

