package com.monocampusconnect.service;

import com.monocampusconnect.dto.MaterialRequest;
import com.monocampusconnect.dto.MaterialStats;
import com.monocampusconnect.exception.ApiException;
import java.time.Instant;
import java.io.IOException;
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
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.ZoneId;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    private final MaterialValidator materialValidator;

    private static final int RATE_LIMIT_WINDOW = 60; // seconds
    private static final int RATE_LIMIT_COUNT = 5; // requests
    private static final int MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    private final ThreadLocal<Instant> lastRequestTime = new ThreadLocal<>();
    private final ThreadLocal<Integer> requestCount = new ThreadLocal<>();

    public MaterialService(MaterialRepository materialRepository, MaterialValidator materialValidator) {
        this.materialRepository = materialRepository;
        this.materialValidator = materialValidator;
    }

    private void applyRateLimit() {
        Instant currentTime = Instant.now();
        Instant lastRequest = lastRequestTime.get();
        
        if (lastRequest == null || currentTime.isAfter(lastRequest.plusSeconds(RATE_LIMIT_WINDOW))) {
            // Reset counter if we're in a new window
            requestCount.set(1);
        } else {
            int count = requestCount.get() != null ? requestCount.get() : 0;
            if (count >= RATE_LIMIT_COUNT) {
                throw new ApiException("Rate limit exceeded. Please wait and try again.", 429);
            }
            requestCount.set(count + 1);
        }
        lastRequestTime.set(currentTime);
    }

    public Material createMaterial(MaterialRequest request, MultipartFile file) throws IOException {
        // Rate limiting
        applyRateLimit();

        // Validate material
        materialValidator.validateMaterial(request);

        // Validate file
        validateFile(file);

        // Check if material code exists
        if (materialRepository.findByMaterialCode(request.getMaterialCode()) != null) {
            throw new ApiException("Material code already exists", 400);
        }

        Material material = new Material();
        material.setMaterialCode(request.getMaterialCode());
        material.setCourseCode(request.getCourseCode());
        material.setTitle(request.getTitle());
        material.setDescription(request.getDescription());
        material.setType(request.getType());
        material.setFileType(file.getContentType());
        material.setFileSize(file.getSize());
        material.setUploadedBy(request.getUploadedBy());
        material.setUploadedDate(new Date());
        
        // Store file content directly in the database
        material.setFileContent(file.getBytes());

        return materialRepository.save(material);
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new ApiException("File is required", 400);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ApiException("File size cannot exceed 50MB", 400);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.matches("application/pdf|application/msword|application/vnd\\.openxmlformats-officedocument\\.wordprocessingml\\.document|application/vnd\\.ms-powerpoint|application/vnd\\.openxmlformats-officedocument\\.presentationml\\.presentation|application/zip|image/jpeg|image/png")) {
            throw new ApiException("Invalid file type. Allowed types: PDF, DOC, DOCX, PPT, PPTX, ZIP, JPG, PNG", 400);
        }
    }

    public Material getMaterial(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ApiException("Material not found", 404));
    }



    public MaterialStats getMaterialStats() {
        List<Material> materials = materialRepository.findAll();
        
        MaterialStats stats = new MaterialStats();
        if (!materials.isEmpty()) {
            Material firstMaterial = materials.get(0);
            stats.setMaterialId(firstMaterial.getId());
            stats.setTitle(firstMaterial.getTitle());
            stats.setCourseName(firstMaterial.getCourseCode());
            stats.setFileName(firstMaterial.getFileType());
            stats.setUploadDate(firstMaterial.getUploadedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            stats.setDownloadCount(0L); // Assuming no download tracking yet
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


}
