package com.hyodore.hyodorebackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyodore.hyodorebackend.dto.DeviceEventDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventListener {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "device-events", groupId = "device-event-group")
    public void listen(String message) {
        try {
            DeviceEventDto dto = objectMapper.readValue(message, DeviceEventDto.class);
            System.out.println("받은 메시지: " + dto);
            // 예: FCM 발송 등 로직 처리
        } catch (Exception e) {
            System.err.println("메시지 처리 실패: " + e.getMessage());
        }
    }
}
