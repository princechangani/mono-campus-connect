package com.monocampusconnect.repository.mongo;

import com.monocampusconnect.model.mongo.ChatMessageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageDocumentRepository extends MongoRepository<ChatMessageDocument, String> {
}

