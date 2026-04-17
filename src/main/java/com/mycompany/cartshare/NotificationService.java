package com.mycompany.cartshare;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class NotificationService {

    public static class Notification {
        private String recipientUsername;
        private String message;
        private LocalDateTime timestamp;
        private boolean isRead;

        public Notification(String recipientUsername, String message) {
            this.recipientUsername = recipientUsername;
            this.message = message;
            this.timestamp = LocalDateTime.now();
            this.isRead = false;
        }

        //getters
        public String getRecipientUsername() {return recipientUsername;}
        public String getMessage() {return message;}
        public LocalDateTime getTimestamp() {return timestamp;}
        public boolean isRead() {return isRead;}

        //read
        public void markAsRead() {
            this.isRead = true;
        }
        //actual notification
        public String toString() {
            return "[" + timestamp + "] " + message + (isRead ? " (read)" : " (unread)");
        }
    }
    
    //notif lists
    private ArrayList<Notification> notifications;
    public NotificationService() {
        this.notifications = new ArrayList<>();
    }

    //send notif
    public void sendNotification(String recipientUsername, String message) {
        notifications.add(new Notification(recipientUsername, message));
    }

    //get all unread notifs for users 
    public ArrayList<Notification> getNotificationsForUser(String username) {
        ArrayList<Notification> userNotifications = new ArrayList<>();
        for (Notification n : notifications) {
            if (n.getRecipientUsername().equals(username)) {
                userNotifications.add(n);
            }
        }
        return userNotifications;
    }

    //read notif
    public void markAsRead(Notification notification) {
        notification.markAsRead();
    }

    //user opens notifs window - marks them all as read
    public void markAllAsRead(String username) {
        for (Notification n : getNotificationsForUser(username)) {
            n.markAsRead();
        }
    }

    //delete notif
    public void deleteNotification(Notification notification) {
        notifications.remove(notification);
    }
    
    //clear all read notifications
    public void clearReadNotifications(String username) {
        notifications.removeIf(n -> n.getRecipientUsername().equals(username) && n.isRead());
    }
}
