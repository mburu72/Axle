package com.tumi.haul.service;

import com.tumi.haul.model.enums.NotificationType;
import com.tumi.haul.model.notification.Notification;
import com.tumi.haul.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
/*
@Service
public class NotificationServiceImp {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImp.class);
    private final NotificationRepository notificationRepository;
   @Autowired
private SimpMessagingTemplate messagingTemplate;
    public NotificationServiceImp(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
@KafkaListener(topics = "quote-alert", groupId = "notification-group")
    public void handleJobQuoteEvent(Notification message){
    log.info("NOTIFICATION: {}", message);
    String clientId = message.getRecipientId();

    Notification notification = new Notification(clientId, message.getMessage());
        notificationRepository.save(notification);
       messagingTemplate.convertAndSend("/topic/client/" + clientId,  message.getMessage());
    }
    @KafkaListener(topics = "quote-status", groupId = "notification-id")
    public void handleQuoteStatusEvent(Notification message){
log.info("NOTIFICATION: {}", message);
       // Notification notification = new Notification(message.getRecipientId(), message.getMessage());
       // notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/drivers/" + message.getRecipientId(), message.getMessage());
    }

    private String extractJobOwnerFromMessage(String message){
        return message;
    }
    private String extractDriverIdFromMessage(String message){
        return message;
    }
}
*/