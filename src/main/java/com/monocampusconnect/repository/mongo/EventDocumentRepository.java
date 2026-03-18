package com.monocampusconnect.repository.mongo;

import com.monocampusconnect.model.mongo.EventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface EventDocumentRepository extends MongoRepository<EventDocument, String> {
    List<EventDocument> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<EventDocument> findByTenantIdAndUserIdOrderByCreatedAtDesc(String tenantId, Long userId);
    List<EventDocument> findByTenantIdAndEventNameAndCreatedAtBetween(String tenantId, String eventName, Date start, Date end);
}
