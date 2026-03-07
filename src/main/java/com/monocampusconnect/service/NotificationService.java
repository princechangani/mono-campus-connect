package com.monocampusconnect.service;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Notification;
import com.monocampusconnect.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    private UUID currentTenant() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    /** Internal: create and save a notification for a user */
    @Transactional
    public Notification createNotification(UUID tenantId, Long userId, String title,
                                           String message, Notification.NotificationType type) {
        Notification n = new Notification();
        n.setTenantId(tenantId);
        n.setUserId(userId);
        n.setTitle(title);
        n.setMessage(message);
        n.setType(type);
        return notificationRepository.save(n);
    }

    /** Get all notifications for the authenticated user */
    public List<Notification> getMyNotifications(Long userId) {
        return notificationRepository.findByTenantIdAndUserIdOrderByCreatedAtDesc(currentTenant(), userId);
    }

    /** Get unread notifications for the authenticated user */
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByTenantIdAndUserIdAndReadFalse(currentTenant(), userId);
    }

    /** Count unread notifications */
    public long countUnread(Long userId) {
        return notificationRepository.countByTenantIdAndUserIdAndReadFalse(currentTenant(), userId);
    }

    /** Mark a single notification as read */
    @Transactional
    public Notification markAsRead(Long notificationId, Long userId) {
        UUID tenantId = currentTenant();
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ApiException("Notification not found", 404));
        if (!tenantId.equals(n.getTenantId()) || !userId.equals(n.getUserId()))
            throw new ApiException("Notification not found", 404);
        n.setRead(true);
        return notificationRepository.save(n);
    }

    /** Mark all notifications as read */
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllReadForUser(currentTenant(), userId);
    }

    /** Delete a notification */
    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        UUID tenantId = currentTenant();
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ApiException("Notification not found", 404));
        if (!tenantId.equals(n.getTenantId()) || !userId.equals(n.getUserId()))
            throw new ApiException("Notification not found", 404);
        notificationRepository.deleteById(notificationId);
    }

    /** Broadcast a notification to all users in the tenant (called by admin) */
    @Transactional
    public void broadcastToTenant(UUID tenantId, List<Long> userIds, String title,
                                  String message, Notification.NotificationType type) {
        userIds.forEach(uid -> createNotification(tenantId, uid, title, message, type));
    }
}

