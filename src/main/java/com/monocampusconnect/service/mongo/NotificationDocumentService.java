package com.monocampusconnect.service.mongo;

import com.monocampusconnect.dto.canonical.mongo.NotificationDocumentRequest;
import com.monocampusconnect.dto.canonical.mongo.NotificationDocumentResponse;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.mongo.NotificationDocument;
import com.monocampusconnect.repository.mongo.NotificationDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationDocumentService {

    private final NotificationDocumentRepository notificationRepository;

    public NotificationDocumentService(NotificationDocumentRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationDocumentResponse create(NotificationDocumentRequest request, Long createdBy) {
        NotificationDocument notification = new NotificationDocument();
        notification.setPublicId(UUID.randomUUID().toString());
        notification.setTenantId(request.getTenantId());
        notification.setNotificationType(request.getNotificationType());
        notification.setCategory(request.getCategory());
        notification.setTitle(request.getTitle().trim());
        notification.setBody(request.getBody().trim());
        notification.setData(request.getData());
        notification.setRecipients(toRecipients(request.getRecipients()));
        notification.setBulkGroup(request.getBulkGroup());
        notification.setScheduledAt(request.getScheduledAt());
        notification.setStatus(request.getStatus() == null ? "pending" : request.getStatus());
        notification.setCreatedAt(new Date());
        notification.setCreatedBy(createdBy == null ? 0L : createdBy);
        notification.setDeleted(false);

        return toResponse(notificationRepository.save(notification));
    }

    public List<NotificationDocumentResponse> getAllByTenant(String tenantId) {
        return notificationRepository.findByTenantIdAndDeletedFalseOrderByCreatedAtDesc(tenantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public NotificationDocumentResponse getByPublicId(String tenantId, String publicId) {
        NotificationDocument notification = notificationRepository.findByPublicIdAndTenantId(publicId, tenantId)
                .filter(n -> !n.isDeleted())
                .orElseThrow(() -> new ApiException("Notification not found", 404));
        return toResponse(notification);
    }

    public NotificationDocumentResponse update(String tenantId, String publicId, NotificationDocumentRequest request) {
        NotificationDocument notification = notificationRepository.findByPublicIdAndTenantId(publicId, tenantId)
                .filter(n -> !n.isDeleted())
                .orElseThrow(() -> new ApiException("Notification not found", 404));

        notification.setNotificationType(request.getNotificationType());
        notification.setCategory(request.getCategory());
        notification.setTitle(request.getTitle().trim());
        notification.setBody(request.getBody().trim());
        notification.setData(request.getData());
        notification.setRecipients(toRecipients(request.getRecipients()));
        notification.setBulkGroup(request.getBulkGroup());
        notification.setScheduledAt(request.getScheduledAt());
        notification.setStatus(request.getStatus() == null ? notification.getStatus() : request.getStatus());
        notification.setUpdatedAt(new Date());

        return toResponse(notificationRepository.save(notification));
    }

    public void delete(String tenantId, String publicId, Long deletedBy) {
        NotificationDocument notification = notificationRepository.findByPublicIdAndTenantId(publicId, tenantId)
                .filter(n -> !n.isDeleted())
                .orElseThrow(() -> new ApiException("Notification not found", 404));

        notification.setDeleted(true);
        notification.setDeletedAt(new Date());
        notification.setDeletedBy(deletedBy);
        notificationRepository.save(notification);
    }

    private List<NotificationDocument.Recipient> toRecipients(List<NotificationDocumentRequest.RecipientRequest> requests) {
        if (requests == null) {
            return null;
        }
        return requests.stream().map(request -> {
            NotificationDocument.Recipient recipient = new NotificationDocument.Recipient();
            recipient.setUserId(request.getUserId());
            recipient.setDeliveredAt(request.getDeliveredAt());
            recipient.setReadAt(request.getReadAt());
            recipient.setFailedAt(request.getFailedAt());
            recipient.setFailureReason(request.getFailureReason());
            return recipient;
        }).toList();
    }

    private List<NotificationDocumentResponse.RecipientResponse> toRecipientResponses(List<NotificationDocument.Recipient> recipients) {
        if (recipients == null) {
            return null;
        }
        return recipients.stream().map(recipient -> {
            NotificationDocumentResponse.RecipientResponse response = new NotificationDocumentResponse.RecipientResponse();
            response.setUserId(recipient.getUserId());
            response.setDeliveredAt(recipient.getDeliveredAt());
            response.setReadAt(recipient.getReadAt());
            response.setFailedAt(recipient.getFailedAt());
            response.setFailureReason(recipient.getFailureReason());
            return response;
        }).toList();
    }

    private NotificationDocumentResponse toResponse(NotificationDocument notification) {
        NotificationDocumentResponse response = new NotificationDocumentResponse();
        response.setId(notification.getId());
        response.setPublicId(notification.getPublicId());
        response.setTenantId(notification.getTenantId());
        response.setNotificationType(notification.getNotificationType());
        response.setCategory(notification.getCategory());
        response.setTitle(notification.getTitle());
        response.setBody(notification.getBody());
        response.setData(notification.getData());
        response.setRecipients(toRecipientResponses(notification.getRecipients()));
        response.setBulkGroup(notification.getBulkGroup());
        response.setSentAt(notification.getSentAt());
        response.setScheduledAt(notification.getScheduledAt());
        response.setStatus(notification.getStatus());
        response.setCreatedAt(notification.getCreatedAt());
        response.setUpdatedAt(notification.getUpdatedAt());
        return response;
    }
}

