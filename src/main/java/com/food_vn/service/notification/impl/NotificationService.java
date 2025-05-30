package com.food_vn.service.notification.impl;

import com.food_vn.model.notification.Notification;
import com.food_vn.model.users.User;
import com.food_vn.model.orders.Orders;
import com.food_vn.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Page<Notification> getNotificationsByUser(User user, Pageable pageable) {
        return notificationRepository.findByUserId(user.getId(), pageable);
    }

    public Page<Notification> getNotificationsForAdmin(Pageable pageable) {
        return notificationRepository.findByReceiverType("admin", pageable);
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
