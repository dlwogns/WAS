package com.hyodore.hyodorebackend.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.hyodore.hyodorebackend.dto.EventRequestDto;
import com.hyodore.hyodorebackend.service.EventService;
import com.hyodore.hyodorebackend.service.FCMService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
  private final EventService eventService;
  private final FCMService fcmService;

  @PostMapping
  public ResponseEntity<String> receiveEvent(@RequestBody EventRequestDto dto)
      throws FirebaseMessagingException {
    eventService.handleEvent(dto);
    return ResponseEntity.ok("이벤트 처리 완료");
  }

  @PostMapping("/test1")
  public ResponseEntity<String> testApi() throws FirebaseMessagingException {
    fcmService.sendNotification("이벤트발생!", "낙상감지!", Map.of("eventCode", "E1"));
    return ResponseEntity.ok("굿");
  }

  @PostMapping("/test2")
  public ResponseEntity<String> test2Api() throws FirebaseMessagingException {
    fcmService.sendNotification("이벤트 동영상 수신", "동영상!", Map.of("videoUrl", "url", "userId", "user123"));
    return ResponseEntity.ok("굿");
  }

}
