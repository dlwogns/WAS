package com.hyodore.hyodorebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hyodore.hyodorebackend.service.MqttPublisherService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mqtt")
public class MqttController {
    private final MqttPublisherService mqttPublisherService;

    @PostMapping("/send")
    public ResponseEntity<String> send(
            @RequestParam String topic,
            @RequestParam String message) {
        mqttPublisherService.publish(topic, message);
        return ResponseEntity.ok("Published to MQTT!");
    }
}