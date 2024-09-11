package com.tumi.haul.model.notification;

import com.tumi.haul.model.enums.NotificationType;
import com.tumi.haul.model.user.Client;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Notification implements Serializable {
    @Id
    private Long id;
    @ManyToOne
    private Client client;
    private String message;
    private Boolean isRead = false;
    private final Date notificationDate = new Date();
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

}
