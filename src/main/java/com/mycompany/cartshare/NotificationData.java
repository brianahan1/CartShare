/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Represents a notification message
 */
package com.mycompany.cartshare;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationData extends AbstractRecord {
    private String timestamp;

    public NotificationData(String message) {
        super(message);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
        timestamp = LocalDateTime.now().format(formatter);
    }

    public String getFormattedNotification() {
        return "[" + timestamp + "] " + name;
    }
}