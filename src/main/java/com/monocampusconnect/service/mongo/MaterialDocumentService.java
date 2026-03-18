package com.monocampusconnect.service.mongo;

import com.monocampusconnect.dto.canonical.mongo.MaterialDocumentRequest;
import com.monocampusconnect.dto.canonical.mongo.MaterialDocumentResponse;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.mongo.MaterialDocument;
import com.monocampusconnect.repository.mongo.MaterialDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MaterialDocumentService {

    private final MaterialDocumentRepository materialRepository;

    public MaterialDocumentService(MaterialDocumentRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public MaterialDocumentResponse create(MaterialDocumentRequest request, Long createdBy) {
        MaterialDocument material = new MaterialDocument();
        material.setPublicId(UUID.randomUUID().toString());
        material.setTenantId(request.getTenantId());
        material.setCourseAssignmentId(request.getCourseAssignmentId());
        material.setCourseId(request.getCourseId());
        material.setFacultyId(request.getFacultyId());
        material.setBatchId(request.getBatchId());
        material.setSemesterNumber(request.getSemesterNumber());
        material.setTitle(request.getTitle().trim());
        material.setDescription(request.getDescription());
        material.setMaterialType(request.getMaterialType());
        material.setFileUrl(request.getFileUrl());
        material.setFileSizeBytes(request.getFileSizeBytes());
        material.setMimeType(request.getMimeType());
        material.setThumbnailUrl(request.getThumbnailUrl());
        material.setTags(request.getTags());
        material.setVisibility(request.getVisibility());
        material.setActive(request.getActive() == null || request.getActive());
        material.setCreatedAt(new Date());
        material.setCreatedBy(createdBy == null ? 0L : createdBy);
        material.setDeleted(false);

        return toResponse(materialRepository.save(material));
    }

    public List<MaterialDocumentResponse> getAllByTenant(String tenantId, Long courseId) {
        List<MaterialDocument> materials = courseId == null
                ? materialRepository.findByTenantIdAndDeletedFalseOrderByCreatedAtDesc(tenantId)
                : materialRepository.findByTenantIdAndCourseIdAndDeletedFalse(tenantId, courseId);
        return materials.stream().map(this::toResponse).toList();
    }

    public MaterialDocumentResponse getByPublicId(String tenantId, String publicId) {
        MaterialDocument material = materialRepository.findByPublicIdAndTenantId(publicId, tenantId)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new ApiException("Material not found", 404));
        return toResponse(material);
    }

    public MaterialDocumentResponse update(String tenantId, String publicId, MaterialDocumentRequest request) {
        MaterialDocument material = materialRepository.findByPublicIdAndTenantId(publicId, tenantId)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new ApiException("Material not found", 404));

        material.setCourseAssignmentId(request.getCourseAssignmentId());
        material.setCourseId(request.getCourseId());
        material.setFacultyId(request.getFacultyId());
        material.setBatchId(request.getBatchId());
        material.setSemesterNumber(request.getSemesterNumber());
        material.setTitle(request.getTitle().trim());
        material.setDescription(request.getDescription());
        material.setMaterialType(request.getMaterialType());
        material.setFileUrl(request.getFileUrl());
        material.setFileSizeBytes(request.getFileSizeBytes());
        material.setMimeType(request.getMimeType());
        material.setThumbnailUrl(request.getThumbnailUrl());
        material.setTags(request.getTags());
        material.setVisibility(request.getVisibility());
        material.setActive(request.getActive() == null ? material.isActive() : request.getActive());
        material.setUpdatedAt(new Date());

        return toResponse(materialRepository.save(material));
    }

    public void delete(String tenantId, String publicId, Long deletedBy) {
        MaterialDocument material = materialRepository.findByPublicIdAndTenantId(publicId, tenantId)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new ApiException("Material not found", 404));

        material.setDeleted(true);
        material.setDeletedAt(new Date());
        material.setDeletedBy(deletedBy);
        materialRepository.save(material);
    }

    private MaterialDocumentResponse toResponse(MaterialDocument material) {
        MaterialDocumentResponse response = new MaterialDocumentResponse();
        response.setId(material.getId());
        response.setPublicId(material.getPublicId());
        response.setTenantId(material.getTenantId());
        response.setCourseAssignmentId(material.getCourseAssignmentId());
        response.setCourseId(material.getCourseId());
        response.setFacultyId(material.getFacultyId());
        response.setBatchId(material.getBatchId());
        response.setSemesterNumber(material.getSemesterNumber());
        response.setTitle(material.getTitle());
        response.setDescription(material.getDescription());
        response.setMaterialType(material.getMaterialType());
        response.setFileUrl(material.getFileUrl());
        response.setFileSizeBytes(material.getFileSizeBytes());
        response.setMimeType(material.getMimeType());
        response.setThumbnailUrl(material.getThumbnailUrl());
        response.setTags(material.getTags());
        response.setVisibility(material.getVisibility());
        response.setActive(material.isActive());
        response.setViewCount(material.getViewCount());
        response.setDownloadCount(material.getDownloadCount());
        response.setCreatedAt(material.getCreatedAt());
        response.setUpdatedAt(material.getUpdatedAt());
        return response;
    }
}

