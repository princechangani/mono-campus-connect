package com.monocampusconnect.service.mongo;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.mongo.ChatMessageDocument;
import com.monocampusconnect.repository.mongo.ChatMessageDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ChatMessageDocumentService {

    private final ChatMessageDocumentRepository chatMessageRepository;

    public ChatMessageDocumentService(ChatMessageDocumentRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    /**
     * Create a new chat message
     */
    public ChatMessageDocument create(ChatMessageDocument message) {
        if (message.getTenantId() == null || message.getTenantId().isEmpty()) {
            throw new ApiException("Tenant ID is required", 400);
        }
        if (message.getPublicId() == null || message.getPublicId().isEmpty()) {
            message.setPublicId(UUID.randomUUID().toString());
        }
        if (message.getCreatedAt() == null) {
            message.setCreatedAt(new Date());
        }
        return chatMessageRepository.save(message);
    }

    /**
     * Get all chat messages
     */
    public List<ChatMessageDocument> getAll() {
        return chatMessageRepository.findAll();
    }

    /**
     * Get chat message by ID
     */
    public ChatMessageDocument getById(String id) {
        return chatMessageRepository.findById(id)
                .orElseThrow(() -> new ApiException("Chat message not found", 404));
    }

    /**
     * Get chat message by public ID
     */
    public ChatMessageDocument getByPublicId(String tenantId, String publicId) {
        return chatMessageRepository.findAll().stream()
                .filter(msg -> tenantId.equals(msg.getTenantId()) && publicId.equals(msg.getPublicId()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Chat message not found", 404));
    }

    /**
     * Update a chat message
     */
    public ChatMessageDocument update(String id, ChatMessageDocument message) {
        ChatMessageDocument existing = chatMessageRepository.findById(id)
                .orElseThrow(() -> new ApiException("Chat message not found", 404));

        if (message.getText() != null)              existing.setText(message.getText());
        if (message.getAttachments() != null)       existing.setAttachments(message.getAttachments());
        if (message.getReactions() != null)         existing.setReactions(message.getReactions());
        if (message.getReadBy() != null)            existing.setReadBy(message.getReadBy());
        if (message.getMessageType() != null)       existing.setMessageType(message.getMessageType());
        if (message.getReplyToMessageId() != null)  existing.setReplyToMessageId(message.getReplyToMessageId());

        existing.setEditedAt(new Date());
        existing.setUpdatedAt(new Date());
        existing.setUpdatedBy(message.getUpdatedBy());

        return chatMessageRepository.save(existing);
    }

    /**
     * Delete a chat message
     */
    public void delete(String id) {
        if (!chatMessageRepository.existsById(id)) {
            throw new ApiException("Chat message not found", 404);
        }
        chatMessageRepository.deleteById(id);
    }

    /**
     * Get messages by channel
     */
    public List<ChatMessageDocument> getByChannelId(String channelId) {
        return chatMessageRepository.findAll().stream()
                .filter(msg -> channelId.equals(msg.getChannelId()))
                .toList();
    }

    /**
     * Get messages by sender
     */
    public List<ChatMessageDocument> getBySenderId(Long senderId) {
        return chatMessageRepository.findAll().stream()
                .filter(msg -> senderId.equals(msg.getSenderId()))
                .toList();
    }

    /**
     * Get messages by tenant and channel
     */
    public List<ChatMessageDocument> getByTenantAndChannel(String tenantId, String channelId) {
        return chatMessageRepository.findAll().stream()
                .filter(msg -> tenantId.equals(msg.getTenantId()) && channelId.equals(msg.getChannelId()))
                .toList();
    }
}

