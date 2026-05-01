'use client';

import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Layout, PageHeader, DataTable } from '@/components/layout/Layout';
import { Button, Card, Input, Select, LoadingSpinner, Modal } from '@/components/ui/Common';
import { usePrograms, useCreateProgram, useUpdateProgram, useDeleteProgram } from '@/hooks/useApi';
import toast from 'react-hot-toast';

const programSchema = z.object({
  name: z.string().min(1, 'Program name is required'),
  code: z.string().min(1, 'Program code is required'),
  departmentId: z.string().min(1, 'Department is required'),
  degreeLevel: z.string().optional(),
  durationYears: z.string().optional().transform(v => v ? parseInt(v) : undefined),
  totalSemesters: z.string().optional().transform(v => v ? parseInt(v) : undefined),
  totalCredits: z.string().optional().transform(v => v ? parseInt(v) : undefined),
});

type ProgramFormData = z.infer<typeof programSchema>;

export default function ProgramsPage() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [deleteId, setDeleteId] = useState<number | null>(null);

  const { data: programs, isLoading } = usePrograms();
  const createProgram = useCreateProgram();
  const updateProgram = useUpdateProgram();
  const deleteProgram = useDeleteProgram();

  const { register, handleSubmit, formState: { errors }, reset, setValue } = useForm<ProgramFormData>({
    resolver: zodResolver(programSchema),
  });

  const onSubmit = async (data: ProgramFormData) => {
    try {
      if (editingId) {
        await updateProgram.mutateAsync({ id: editingId, data: { ...data, departmentId: parseInt(data.departmentId) } });
        setEditingId(null);
      } else {
        await createProgram.mutateAsync({ ...data, departmentId: parseInt(data.departmentId) });
      }
      reset();
      setIsModalOpen(false);
      toast.success(editingId ? 'Program updated successfully' : 'Program created successfully');
    } catch (error) {
      toast.error('Failed to save program');
    }
  };

  const handleEdit = (program: any) => {
    setEditingId(program.id);
    setValue('name', program.name);
    setValue('code', program.code);
    setValue('departmentId', program.departmentId.toString());
    setValue('degreeLevel', program.degreeLevel || '');
    setValue('durationYears', program.durationYears?.toString() || '');
    setValue('totalSemesters', program.totalSemesters?.toString() || '');
    setValue('totalCredits', program.totalCredits?.toString() || '');
    setIsModalOpen(true);
  };

  const handleDelete = async () => {
    if (!deleteId) return;
    try {
      await deleteProgram.mutateAsync(deleteId);
      setDeleteId(null);
      toast.success('Program deleted successfully');
    } catch (error) {
      toast.error('Failed to delete program');
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
    <Layout navItems={navItems} title="Programs">
      <PageHeader
        title="Programs Management"
        description="Manage degree programs offered by your institution"
        action={{
          label: '+ New Program',
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
              { key: 'code', label: 'Code' },
              { key: 'name', label: 'Name' },
              { key: 'degreeLevel', label: 'Degree Level' },
              { key: 'durationYears', label: 'Duration (Years)' },
              { key: 'totalSemesters', label: 'Total Semesters' },
            ]}
            data={programs || []}
            actions={(program) => (
              <div className="flex gap-2">
                <Button size="sm" variant="secondary" onClick={() => handleEdit(program)}>
                  Edit
                </Button>
                <Button
                  size="sm"
                  variant="danger"
                  onClick={() => setDeleteId(program.id)}
                >
                  Delete
                </Button>
              </div>
            )}
          />
        )}
      </Card>

      {/* Create/Edit Modal */}
      <Modal
        isOpen={isModalOpen}
        title={editingId ? 'Edit Program' : 'Create New Program'}
        onClose={() => {
          setIsModalOpen(false);
          setEditingId(null);
          reset();
        }}
        onSubmit={handleSubmit(onSubmit)}
        submitText={editingId ? 'Update' : 'Create'}
      >
        <form className="space-y-4">
          <Input
            label="Program Name"
            placeholder="e.g., Bachelor of Computer Science"
            {...register('name')}
            error={errors.name?.message}
          />

          <Input
            label="Program Code"
            placeholder="e.g., BCSCI"
            {...register('code')}
            error={errors.code?.message}
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
            label="Degree Level"
            placeholder="e.g., Bachelor, Master"
            {...register('degreeLevel')}
          />

          <Input
            label="Duration (Years)"
            type="number"
            placeholder="e.g., 4"
            {...register('durationYears')}
          />

          <Input
            label="Total Semesters"
            type="number"
            placeholder="e.g., 8"
            {...register('totalSemesters')}
          />

          <Input
            label="Total Credits"
            type="number"
            placeholder="e.g., 120"
            {...register('totalCredits')}
          />
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={deleteId !== null}
        title="Delete Program"
        onClose={() => setDeleteId(null)}
        onSubmit={handleDelete}
        submitText="Delete"
      >
        <p className="text-gray-600">Are you sure you want to delete this program? This action cannot be undone.</p>
      </Modal>
    </Layout>
  );
}

