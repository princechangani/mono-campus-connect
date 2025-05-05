package com.monocampusconnect.repository;

import com.monocampusconnect.model.Material;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Material findByMaterialCode(String materialCode);
    List<Material> findByCourseCode(String courseCode);


    List<Material> findByType(String type);
    List<Material> findByUploadedBy(String uploadedBy);
    
    // Pagination
    List<Material> findTopByOrderByUploadedDateDesc(Pageable pageable);
    
    // Date range queries
    List<Material> findByUploadedDateBetween(Date startDate, Date endDate);
    
    // Search queries
    List<Material> findByTitleContainingIgnoreCase(String title);
    List<Material> findByDescriptionContainingIgnoreCase(String description);
    List<Material> findByCourseCodeAndType(String courseCode, String type);
    
    // Statistics
    long countByCourseCode(String courseCode);
    long countByType(String type);
    long countByUploadedBy(String uploadedBy);
    
    // Download count queries
    List<Material> findByDownloadCountGreaterThan(int count);
    List<Material> findByDownloadCountLessThan(int count);
    
    // Custom queries
    List<Material> findByUploadedDateAfter(Date date);
    List<Material> findByUploadedDateBefore(Date date);
    List<Material> findByFileType(String fileType);
}
