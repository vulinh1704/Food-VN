package com.food_vn.service.notification.impl;

import com.food_vn.model.notification.Notification;
import com.food_vn.model.users.User;
import com.food_vn.model.orders.Orders;
import com.food_vn.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<Notification> getNotificationsByUserWithFilter(User user, String filter) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start, end;
        end = switch (filter) {
            case "day" -> {
                start = now.toLocalDate().atStartOfDay();
                yield start.plusDays(1);
            }
            case "month" -> {
                start = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
                yield start.plusMonths(1);
            }
            case "year" -> {
                start = now.withDayOfYear(1).toLocalDate().atStartOfDay();
                yield start.plusYears(1);
            }
            default -> {
                start = null;
                yield null;
            }
        };
        return notificationRepository.findByUserIdAndCreatedAtBetween(user.getId(), start, end);
    }

    public List<Notification> getNotificationsForAdminWithFilter(String filter) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start, end;
        end = switch (filter) {
            case "day" -> {
                start = now.toLocalDate().atStartOfDay();
                yield start.plusDays(1);
            }
            case "month" -> {
                start = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
                yield start.plusMonths(1);
            }
            case "year" -> {
                start = now.withDayOfYear(1).toLocalDate().atStartOfDay();
                yield start.plusYears(1);
            }
            default -> {
                start = null;
                yield null;
            }
        };
        return notificationRepository.findByReceiverTypeAndCreatedAtBetween("admin", start, end);
    }

    public Notification sendNotification(User user, Orders order, String receiverType, String type, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setOrders(order);
        notification.setReceiverType(receiverType);
        notification.setType(type);
        notification.setMessage(message);
        notification.setRead(false);
        Notification saved = notificationRepository.save(notification);
        // Send realtime notification via WebSocket
        Notification newNotification = notificationRepository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        if ("admin".equals(receiverType)) {
            messagingTemplate.convertAndSend("/topic/notifications/admin", newNotification);
        } else {
            messagingTemplate.convertAndSend("/topic/notifications/" + user.getId(), newNotification);
        }
        return saved;
    }
}
