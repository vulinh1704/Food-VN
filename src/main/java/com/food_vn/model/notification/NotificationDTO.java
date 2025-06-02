package com.food_vn.model.notification;

public class NotificationDTO {
    private Long id;
    private Boolean isRead;
    private Long userId;
    private Long orderId;
    private String receiverType;
    private String type;
    private String message;
    private String createdAt;

    public NotificationDTO() {}

    public NotificationDTO(Long id, Boolean isRead, Long userId, Long orderId, String receiverType, String type, String message, String createdAt) {
        this.id = id;
        this.isRead = isRead;
        this.userId = userId;
        this.orderId = orderId;
        this.receiverType = receiverType;
        this.type = type;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getReceiverType() { return receiverType; }
    public void setReceiverType(String receiverType) { this.receiverType = receiverType; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
