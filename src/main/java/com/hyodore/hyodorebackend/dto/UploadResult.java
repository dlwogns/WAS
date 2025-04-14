package com.hyodore.hyodorebackend.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UploadResult {

  private String syncedAt;
  private String photoId;

  public UploadResult(String photoId) {
    this.photoId = photoId;
    this.syncedAt = LocalDateTime.now().toString();
  }
}
