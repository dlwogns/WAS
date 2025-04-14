package com.hyodore.hyodorebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncQueueMessage {

  private String type; // "upload"
  private String photoId;
  private String photoUrl;
  private String userId;
  private String familyId;
  private String requestId; // 동기 응답 매핑용
}
