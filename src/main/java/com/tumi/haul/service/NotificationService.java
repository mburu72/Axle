package com.tumi.haul.service;

import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.notification.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    public Optional<Job> createJob(Notification notification);
    public void deleteNotification(Long idChar);
    public List<Notification> getAllNotification();
    public Optional<Notification>getNotificationById(Long idChar);

}
