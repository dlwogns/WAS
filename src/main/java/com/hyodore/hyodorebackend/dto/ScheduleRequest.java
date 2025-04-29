package com.hyodore.hyodorebackend.dto;

import lombok.Data;

@Data
public class ScheduleRequest {
  private String scheduleId;
  private String userId;
  private String scheduleDesc;
  private String scheduleDate;
}
