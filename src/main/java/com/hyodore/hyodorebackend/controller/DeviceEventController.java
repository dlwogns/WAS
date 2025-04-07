package com.hyodore.hyodorebackend.controller;

import com.hyodore.hyodorebackend.dto.DeviceEventDto;
import com.hyodore.hyodorebackend.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceEventController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/event")
    public ResponseEntity<String> publish(@RequestBody DeviceEventDto dto) {
        kafkaProducerService.sendEvent(dto);
        return ResponseEntity.ok("Event published");
    }
}
