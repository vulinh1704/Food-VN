package com.food_vn.service.notification.impl;
import com.food_vn.model.notification.Notification;
import com.food_vn.model.users.User;
import com.food_vn.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Page<Notification> getNotificationsByUser(User user, Pageable pageable) {
        return notificationRepository.findByUserId(user.getId(), pageable);
    }

    public Page<Notification> getNotificationsForAdmin(Pageable pageable) {
        return notificationRepository.findByReceiverType("admin", pageable);
    }
}

