package com.tumi.haul.model.notification;

import com.tumi.haul.model.enums.NotificationType;
import com.tumi.haul.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
@Getter
public class Notification implements Serializable {
    @Id
    private String id;
    @PrePersist
    public void generateId(){
        if (this.id == null){
            this.id = "j_" + UUID.randomUUID().toString().substring(0, 8).toLowerCase();
        }
    }
    private String recipientId;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    public Notification(String recipientId, String message) {
        this.recipientId = recipientId;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }
}
