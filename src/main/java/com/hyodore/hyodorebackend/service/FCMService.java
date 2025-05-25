package com.hyodore.hyodorebackend.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

  @Value("${fcm.token}")
  private String token;


  public void sendNotification(String title, String body, Map<String, String> data) throws FirebaseMessagingException {
    Notification notification = Notification.builder()
        .setTitle(title)
        .setBody(body)
        .build();

    Message message = Message.builder()
        .setToken(token)
        .setNotification(notification)
        .putAllData(data)
        .build();

    String response = FirebaseMessaging.getInstance().send(message);
    System.out.println("FCM 응답: " + response);
  }
}
