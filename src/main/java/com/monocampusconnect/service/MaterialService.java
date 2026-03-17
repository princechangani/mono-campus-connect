package com.monocampusconnect.service;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.dto.MaterialRequest;
import com.monocampusconnect.dto.MaterialStats;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Material;
import com.monocampusconnect.repository.MaterialRepository;
import com.monocampusconnect.validator.MaterialValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialValidator materialValidator;

    private static final int MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final int RATE_LIMIT_WINDOW = 60;
    private static final int RATE_LIMIT_COUNT = 5;
    private final ThreadLocal<Instant> lastRequestTime = new ThreadLocal<>();
    private final ThreadLocal<Integer> requestCount = new ThreadLocal<>();

    public MaterialService(MaterialRepository materialRepository, MaterialValidator materialValidator) {
        this.materialRepository = materialRepository;
        this.materialValidator = materialValidator;
    }

    private UUID currentTenant() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    private void applyRateLimit() {
        Instant now = Instant.now();
        Instant last = lastRequestTime.get();
        if (last == null || now.isAfter(last.plusSeconds(RATE_LIMIT_WINDOW))) {
            requestCount.set(1);
        } else {
            int count = requestCount.get() != null ? requestCount.get() : 0;
            if (count >= RATE_LIMIT_COUNT) throw new ApiException("Rate limit exceeded. Try again later.", 429);
            requestCount.set(count + 1);
        }
        lastRequestTime.set(now);
    }

    public Material createMaterial(MaterialRequest request, MultipartFile file) throws IOException {
        applyRateLimit();
        materialValidator.validateMaterial(request);
        validateFile(file);
        if (materialRepository.findByMaterialCode(request.getMaterialCode()) != null) {
            throw new ApiException("Material code already exists", 400);
        }
        Material material = new Material();
        material.setTenantId(currentTenant());
        material.setMaterialCode(request.getMaterialCode());
        material.setCourseCode(request.getCourseCode());
        material.setTitle(request.getTitle());
        material.setDescription(request.getDescription());
        material.setType(request.getType());
        material.setFileType(file.getContentType());
        material.setFileSize(file.getSize());
        material.setUploadedBy(request.getUploadedBy());
        material.setUploadedDate(new Date());
        material.setFileContent(file.getBytes());
        return materialRepository.save(material);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new ApiException("File is required", 400);
        if (file.getSize() > MAX_FILE_SIZE) throw new ApiException("File size cannot exceed 50MB", 400);
        String ct = file.getContentType();
        if (ct == null || !ct.matches(
                "application/pdf|application/msword|" +
                "application/vnd\\.openxmlformats-officedocument\\.wordprocessingml\\.document|" +
                "application/vnd\\.ms-powerpoint|" +
                "application/vnd\\.openxmlformats-officedocument\\.presentationml\\.presentation|" +
                "application/zip|image/jpeg|image/png|video/mp4|video/mpeg")) {
            throw new ApiException("Invalid file type. Allowed: PDF, DOC, DOCX, PPT, PPTX, ZIP, JPG, PNG, MP4", 400);
        }
    }

    public Material getMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ApiException("Material not found", 404));
        if (!currentTenant().equals(material.getTenantId()))
            throw new ApiException("Material not found in this college", 404);
        // Increment download count
        material.setDownloadCount(material.getDownloadCount() + 1);
        return materialRepository.save(material);
    }

    public List<Material> getAllMaterials() {
        return materialRepository.findByTenantId(currentTenant());
    }

    public List<Material> getMaterialsByCourse(String courseCode) {
        return materialRepository.findByTenantIdAndCourseCode(currentTenant(), courseCode);
    }

    public Material updateMaterial(Long id, MaterialRequest request, MultipartFile file) throws IOException {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ApiException("Material not found", 404));
        if (!currentTenant().equals(material.getTenantId()))
            throw new ApiException("Material not found in this college", 404);
        material.setTitle(request.getTitle());
        material.setDescription(request.getDescription());
        material.setType(request.getType());
        material.setLastUpdatedDate(new Date());
        if (file != null && !file.isEmpty()) {
            validateFile(file);
            material.setFileContent(file.getBytes());
            material.setFileType(file.getContentType());
            material.setFileSize(file.getSize());
        }
        return materialRepository.save(material);
    }

    public void deleteMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ApiException("Material not found", 404));
        if (!currentTenant().equals(material.getTenantId()))
            throw new ApiException("Material not found in this college", 404);
        materialRepository.deleteById(id);
    }

    public MaterialStats getMaterialStats() {
        List<Material> materials = materialRepository.findByTenantId(currentTenant());
        MaterialStats stats = new MaterialStats();
        if (!materials.isEmpty()) {
            Material first = materials.get(0);
            stats.setMaterialId(first.getMaterialId());
            stats.setTitle(first.getTitle());
            stats.setCourseName(first.getCourseCode());
            stats.setFileName(first.getFileType());
            stats.setUploadDate(first.getUploadedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            stats.setDownloadCount(0L);
        }
        return stats;
    }

    public List<Material> getMaterialsByType(String type) {
        return materialRepository.findByType(type);
    }

    public List<Material> getMaterialsByUploader(String uploader) {
        return materialRepository.findByUploadedBy(uploader);
    }

    public List<Material> getRecentMaterials(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return materialRepository.findTopByOrderByUploadedDateDesc(pageable);
    }

    public Material getMaterialByPublicId(UUID publicId) {
        Material material = materialRepository.findByMaterialsPublicIdAndTenantId(publicId, currentTenant())
                .orElseThrow(() -> new ApiException("Material not found", 404));
        material.setDownloadCount(material.getDownloadCount() + 1);
        return materialRepository.save(material);
    }

    public Material updateMaterialByPublicId(UUID publicId, MaterialRequest request, MultipartFile file) throws IOException {
        Material material = materialRepository.findByMaterialsPublicIdAndTenantId(publicId, currentTenant())
                .orElseThrow(() -> new ApiException("Material not found", 404));
        return updateMaterial(material.getId(), request, file);
    }

    public void deleteMaterialByPublicId(UUID publicId) {
        Material material = materialRepository.findByMaterialsPublicIdAndTenantId(publicId, currentTenant())
                .orElseThrow(() -> new ApiException("Material not found", 404));
        deleteMaterial(material.getId());
    }
}
