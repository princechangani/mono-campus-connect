package com.monocampusconnect.controller;

import com.monocampusconnect.config.JwtConfig;
import com.monocampusconnect.model.Notification;
import com.monocampusconnect.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtConfig jwtConfig;

    private Long resolveUserId(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return jwtConfig.extractUserId(header.substring(7));
        }
        throw new RuntimeException("Unable to resolve user from token");
    }

    /** GET /api/notifications — all notifications for current user */
    @GetMapping
    public ResponseEntity<List<Notification>> getMyNotifications(HttpServletRequest request) {
        return ResponseEntity.ok(notificationService.getMyNotifications(resolveUserId(request)));
    }

    /** GET /api/notifications/unread */
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnread(HttpServletRequest request) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(resolveUserId(request)));
    }

    /** GET /api/notifications/unread/count */
    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(HttpServletRequest request) {
        return ResponseEntity.ok(Map.of("count", notificationService.countUnread(resolveUserId(request))));
    }

    /** PUT /api/notifications/{id}/read */
    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(notificationService.markAsRead(id, resolveUserId(request)));
    }

    /** PUT /api/notifications/read-all */
    @PutMapping("/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead(HttpServletRequest request) {
        notificationService.markAllAsRead(resolveUserId(request));
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }

    /** DELETE /api/notifications/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteNotification(@PathVariable Long id,
                                                                   HttpServletRequest request) {
        notificationService.deleteNotification(id, resolveUserId(request));
        return ResponseEntity.ok(Map.of("message", "Notification deleted"));
    }
}

