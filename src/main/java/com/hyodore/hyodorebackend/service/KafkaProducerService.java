package com.hyodore.hyodorebackend.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyodore.hyodorebackend.dto.DeviceEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TOPIC = "device-events";

    public void sendEvent(DeviceEventDto dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send(TOPIC, json)
                    .whenComplete((result, exception) -> {
                        if(exception == null) {
                            System.out.println("메시지 전송 성공");
                        }else {
                            System.out.println("메시지 전송 실패 : " + exception.getMessage());
                        }
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
