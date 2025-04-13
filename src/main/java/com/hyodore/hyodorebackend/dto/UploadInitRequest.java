package com.hyodore.hyodorebackend.dto;

import lombok.Data;

@Data
public class UploadInitRequest {

  private String fileName;
  private String contentType;
}
