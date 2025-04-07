package com.hyodore.hyodorebackend.dto;

import lombok.Data;

@Data
public class DeviceEventDto {
    private String deviceId;
    private String userId;
    private String eventType;
    private String message;
}
