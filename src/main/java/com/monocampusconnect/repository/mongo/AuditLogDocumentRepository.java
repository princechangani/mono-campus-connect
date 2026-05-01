package com.monocampusconnect.repository.mongo;

import com.monocampusconnect.model.mongo.AuditLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogDocumentRepository extends MongoRepository<AuditLogDocument, String> {
}

