package com.hyodore.hyodorebackend.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.hyodore.hyodorebackend.dto.EventRequestDto;
import com.hyodore.hyodorebackend.service.EventService;
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

  @PostMapping
  public ResponseEntity<String> receiveEvent(@RequestBody EventRequestDto dto)
      throws FirebaseMessagingException {
    eventService.handleEvent(dto);
    return ResponseEntity.ok("이벤트 처리 완료");
  }

}
