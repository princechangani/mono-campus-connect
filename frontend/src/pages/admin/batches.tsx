import React, { useState } from 'react';
import { useRouter } from 'next/router';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Layout, PageHeader, DataTable } from '@/components/layout/Layout';
import { Button, Card, Input, Select, LoadingSpinner, Modal } from '@/components/ui/Common';
import { useBatches, useCreateBatch, useUpdateBatch, useDeleteBatch } from '@/hooks/useApi';
import toast from 'react-hot-toast';

const batchSchema = z.object({
  name: z.string().min(1, 'Batch name is required'),
  programId: z.string().min(1, 'Program is required'),
  academicYearId: z.string().optional(),
  currentSemester: z.string().optional().transform(v => v ? parseInt(v) : undefined),
  maxStudents: z.string().optional().transform(v => v ? parseInt(v) : undefined),
});

type BatchFormData = z.infer<typeof batchSchema>;

const BatchesPage = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [deleteId, setDeleteId] = useState<number | null>(null);

  const { data: batches, isLoading } = useBatches();
  const createBatch = useCreateBatch();
  const updateBatch = useUpdateBatch();
  const deleteBatch = useDeleteBatch();

  const { register, handleSubmit, formState: { errors }, reset, setValue } = useForm<BatchFormData>({
    resolver: zodResolver(batchSchema),
  });

  const onSubmit = async (data: BatchFormData) => {
    try {
      if (editingId) {
        await updateBatch.mutateAsync({ id: editingId, data: { ...data, programId: parseInt(data.programId) } });
        setEditingId(null);
      } else {
        await createBatch.mutateAsync({ ...data, programId: parseInt(data.programId) });
      }
      reset();
      setIsModalOpen(false);
      toast.success(editingId ? 'Batch updated successfully' : 'Batch created successfully');
    } catch (error) {
      toast.error('Failed to save batch');
    }
  };

  const handleEdit = (batch: any) => {
    setEditingId(batch.id);
    setValue('name', batch.name);
    setValue('programId', batch.programId.toString());
    setValue('currentSemester', batch.currentSemester?.toString() || '');
    setValue('maxStudents', batch.maxStudents?.toString() || '');
    setIsModalOpen(true);
  };

  const handleDelete = async () => {
    if (!deleteId) return;
    try {
      await deleteBatch.mutateAsync(deleteId);
      setDeleteId(null);
      toast.success('Batch deleted successfully');
    } catch (error) {
      toast.error('Failed to delete batch');
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
    <Layout navItems={navItems} title="Batches">
      <PageHeader
        title="Batches Management"
        description="Manage student batches"
        action={{
          label: '+ New Batch',
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
              { key: 'name', label: 'Name' },
              { key: 'programId', label: 'Program ID' },
              { key: 'currentSemester', label: 'Current Semester' },
              { key: 'maxStudents', label: 'Max Students' },
            ]}
            data={batches || []}
            actions={(batch) => (
              <div className="flex gap-2">
                <Button size="sm" variant="secondary" onClick={() => handleEdit(batch)}>
                  Edit
                </Button>
                <Button
                  size="sm"
                  variant="danger"
                  onClick={() => setDeleteId(batch.id)}
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
        title={editingId ? 'Edit Batch' : 'Create New Batch'}
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
            label="Batch Name"
            placeholder="e.g., Batch 2024-A"
            {...register('name')}
            error={errors.name?.message}
          />

          <Select
            label="Program"
            options={[
              { value: '1', label: 'Bachelor of Computer Science' },
              { value: '2', label: 'Bachelor of Engineering' },
            ]}
            {...register('programId')}
            error={errors.programId?.message}
          />

          <Input
            label="Current Semester"
            type="number"
            placeholder="e.g., 1"
            {...register('currentSemester')}
          />

          <Input
            label="Max Students"
            type="number"
            placeholder="e.g., 60"
            {...register('maxStudents')}
          />
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={deleteId !== null}
        title="Delete Batch"
        onClose={() => setDeleteId(null)}
        onSubmit={handleDelete}
        submitText="Delete"
      >
        <p className="text-gray-600">Are you sure you want to delete this batch? This action cannot be undone.</p>
      </Modal>
    </Layout>
  );
};

export default BatchesPage;

