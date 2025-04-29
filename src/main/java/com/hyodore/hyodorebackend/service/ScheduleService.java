package com.hyodore.hyodorebackend.service;

import com.hyodore.hyodorebackend.dto.ScheduleRequest;
import com.hyodore.hyodorebackend.entity.Schedule;
import com.hyodore.hyodorebackend.repository.ScheduleRepository;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

  private final ScheduleRepository scheduleRepository;
  private final UserService userService;

  public void saveSchedule(ScheduleRequest request) {
    String familyId = userService.getFamilyIdByUserId(request.getUserId());
    scheduleRepository.save(
        Schedule.builder()
            .scheduleId(request.getScheduleId())
            .familyId(familyId)
            .userId(request.getUserId())
            .scheduleDesc(request.getScheduleDesc())
            .scheduleDate(OffsetDateTime.parse(request.getScheduleDate()))
            .build()
    );
  }

  public void deleteSchedule(ScheduleRequest request) {
    scheduleRepository.deleteById(request.getScheduleId());
  }
}
