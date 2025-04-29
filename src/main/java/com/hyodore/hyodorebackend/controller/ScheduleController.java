package com.hyodore.hyodorebackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyodore.hyodorebackend.dto.ScheduleRequest;
import com.hyodore.hyodorebackend.service.MqttPublisherService;
import com.hyodore.hyodorebackend.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

  private final ScheduleService scheduleService;
  private final MqttPublisherService mqttPublisherService;
  private final ObjectMapper objectMapper;

  @PostMapping("/upload")
  public ResponseEntity<String> uploadSchedule(
      @RequestBody ScheduleRequest request
  ) throws JsonProcessingException {
    scheduleService.saveSchedule(request);
    mqttPublisherService.publish("schedule/upload", objectMapper.writeValueAsString(request));
    return ResponseEntity.ok("Schedule uploaded");
  }

  @PostMapping("/delete")
  public ResponseEntity<String> deleteSchedule(
      @RequestBody ScheduleRequest request
  ) throws JsonProcessingException {
    scheduleService.deleteSchedule(request);
    mqttPublisherService.publish("schedule/delete", objectMapper.writeValueAsString(request));
    return ResponseEntity.ok("Schedule deleted");
  }
}
