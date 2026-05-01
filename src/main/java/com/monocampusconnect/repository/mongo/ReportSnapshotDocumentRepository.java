package com.monocampusconnect.repository.mongo;

import com.monocampusconnect.model.mongo.ReportSnapshotDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportSnapshotDocumentRepository extends MongoRepository<ReportSnapshotDocument, String> {
}

