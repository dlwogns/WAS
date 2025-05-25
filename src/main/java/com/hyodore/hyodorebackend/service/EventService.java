package com.hyodore.hyodorebackend.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.hyodore.hyodorebackend.dto.EventRequestDto;
import com.hyodore.hyodorebackend.entity.Device;
import com.hyodore.hyodorebackend.entity.Event;
import com.hyodore.hyodorebackend.entity.EventType;
import com.hyodore.hyodorebackend.repository.DeviceRepository;
import com.hyodore.hyodorebackend.repository.EventRepository;
import com.hyodore.hyodorebackend.repository.EventTypeRepository;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

  private final EventRepository eventRepository;
  private final EventTypeRepository eventTypeRepository;
  private final FCMService fcmService;
  private final DeviceRepository deviceRepository;


  public void handleEvent(EventRequestDto dto) throws FirebaseMessagingException {
    // 이벤트 저장
    Event event = Event.builder()
        .eventCode(dto.getEventCode())
        .deviceId(dto.getDeviceId())
        .familyId(getFamilyIdByDeviceId(dto.getDeviceId()))
        .createdAt(LocalDateTime.now())
        .build();

    eventRepository.save(event);

    // 메시지 DB에서 조회
    String message = eventTypeRepository.findById(dto.getEventCode())
        .map(EventType::getDescription)
        .orElse("알 수 없는 이벤트");

    // FCM 전송
    fcmService.sendNotification("이벤트 발생!", message, Map.of("eventCode", dto.getEventCode()));
  }

  private String getFamilyIdByDeviceId(String deviceId) {
    return deviceRepository.findById(deviceId)
        .map(Device::getFamilyId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 디바이스입니다: " + deviceId));
  }
}
