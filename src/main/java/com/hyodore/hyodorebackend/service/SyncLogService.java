package com.hyodore.hyodorebackend.service;

import com.hyodore.hyodorebackend.entity.SyncLog;
import com.hyodore.hyodorebackend.repository.SyncLogRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncLogService {

  private final SyncLogRepository syncLogRepository;
  private final UserService userService;


  public LocalDateTime findLastSyncedAtByUserId(String userId) {
    String familyId = userService.getFamilyIdByUserId(userId);
    return syncLogRepository.findTopByFamilyIdOrderByLastSyncedDesc(familyId)
        .map(SyncLog::getLastSynced)
        .orElse(LocalDateTime.MIN); // 동기화 기록 없으면 최초로 간주
  }

  public LocalDateTime findLastSyncedAtByDeviceId(String deviceId) {
    return syncLogRepository.findTopByDeviceIdOrderByLastSyncedDesc(deviceId)
        .map(SyncLog::getLastSynced)
        .orElse(LocalDateTime.MIN);
  }

  public void saveSyncLog(String familyId, String deviceId, LocalDateTime lastSynced) {
    syncLogRepository.save(SyncLog.builder()
        .familyId(familyId)
        .deviceId(deviceId)
        .lastSynced(lastSynced)
        .build());
  }

  public void saveSyncLog(String userId, LocalDateTime lastSynced) {
    String familyId = userService.getFamilyIdByUserId(userId);
    syncLogRepository.save(SyncLog.builder()
        .familyId(familyId)
        .lastSynced(lastSynced)
        .build());
  }
}

