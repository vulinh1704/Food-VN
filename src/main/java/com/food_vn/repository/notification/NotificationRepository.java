package com.food_vn.repository.notification;

import com.food_vn.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserId(Long id, Pageable pageable);
    Page<Notification> findByReceiverType(String receiverType, Pageable pageable);
}

