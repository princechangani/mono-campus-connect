package com.monocampusconnect.repository.mongo;

import com.monocampusconnect.model.mongo.NotificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationDocumentRepository extends MongoRepository<NotificationDocument, String> {
    Optional<NotificationDocument> findByPublicIdAndTenantId(String publicId, String tenantId);
    List<NotificationDocument> findByTenantIdAndDeletedFalseOrderByCreatedAtDesc(String tenantId);
    long countByTenantIdAndDeletedFalseAndStatus(String tenantId, String status);
}
