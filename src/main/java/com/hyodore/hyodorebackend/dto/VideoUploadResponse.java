package com.hyodore.hyodorebackend.dto;

import lombok.Data;

@Data
public class VideoUploadResponse {
  private String videoUrl;
  private String message;

  public VideoUploadResponse(String videoUrl, String message) {this.videoUrl = videoUrl;this.message = message;}
}
