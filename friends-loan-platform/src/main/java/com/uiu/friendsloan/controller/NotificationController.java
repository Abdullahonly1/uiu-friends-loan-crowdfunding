package com.uiu.friendsloan.controller;

import com.uiu.friendsloan.entity.Notification;
import com.uiu.friendsloan.service.NotificationService;
import com.uiu.friendsloan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;        // ← এটা যোগ করো

    // সব নোটিফিকেশন দেখানো
    @GetMapping
    public ResponseEntity<List<Notification>> getMyNotifications() {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    // Unread count (Bell Icon এর জন্য)
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    // একটা নোটিফিকেশন Read করা
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}