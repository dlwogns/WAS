package com.hyodore.hyodorebackend.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FCMService {
  public void sendNotification(String targetToken, String title, String body) throws FirebaseMessagingException {
    Notification notification = Notification.builder()
        .setTitle(title)
        .setBody(body)
        .build();

    Message message = Message.builder()
        .setToken(targetToken)
        .setNotification(notification)
        .build();

    String response = FirebaseMessaging.getInstance().send(message);
    System.out.println("FCM 응답: " + response);
  }
}
