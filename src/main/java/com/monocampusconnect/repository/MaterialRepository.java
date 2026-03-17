package com.monocampusconnect.repository;

import com.monocampusconnect.model.Material;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Material findByMaterialCode(String materialCode);
    List<Material> findByCourseCode(String courseCode);
    List<Material> findByTenantId(UUID tenantId);
    List<Material> findByTenantIdAndCourseCode(UUID tenantId, String courseCode);
    List<Material> findByType(String type);
    List<Material> findByUploadedBy(String uploadedBy);
    List<Material> findTopByOrderByUploadedDateDesc(Pageable pageable);
    List<Material> findByUploadedDateBetween(Date startDate, Date endDate);
    List<Material> findByTitleContainingIgnoreCase(String title);
    List<Material> findByDescriptionContainingIgnoreCase(String description);
    List<Material> findByCourseCodeAndType(String courseCode, String type);
    long countByCourseCode(String courseCode);
    long countByType(String type);
    long countByUploadedBy(String uploadedBy);
    long countByTenantId(UUID tenantId);
    List<Material> findByDownloadCountGreaterThan(int count);
    List<Material> findByDownloadCountLessThan(int count);
    List<Material> findByUploadedDateAfter(Date date);
    List<Material> findByUploadedDateBefore(Date date);
    List<Material> findByFileType(String fileType);
    java.util.Optional<Material> findByMaterialsPublicIdAndTenantId(UUID materialsPublicId, UUID tenantId);
}
