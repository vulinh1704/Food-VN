package com.food_vn.repository.notification;

import com.food_vn.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND (:start IS NULL OR n.createdAt >= :start) AND (:end IS NULL OR n.createdAt < :end) ORDER BY n.createdAt DESC")
    List<Notification> findByUserIdAndCreatedAtBetween(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT n FROM Notification n WHERE n.receiverType = :receiverType AND (:start IS NULL OR n.createdAt >= :start) AND (:end IS NULL OR n.createdAt < :end) ORDER BY n.createdAt DESC")
    List<Notification> findByReceiverTypeAndCreatedAtBetween(@Param("receiverType") String receiverType, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
