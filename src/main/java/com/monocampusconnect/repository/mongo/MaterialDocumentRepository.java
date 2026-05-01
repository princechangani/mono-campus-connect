package com.monocampusconnect.repository.mongo;

import com.monocampusconnect.model.mongo.MaterialDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MaterialDocumentRepository extends MongoRepository<MaterialDocument, String> {
    Optional<MaterialDocument> findByPublicIdAndTenantId(String publicId, String tenantId);
    List<MaterialDocument> findByTenantIdAndDeletedFalseOrderByCreatedAtDesc(String tenantId);
    List<MaterialDocument> findByTenantIdAndCourseIdAndDeletedFalse(String tenantId, Long courseId);
}
