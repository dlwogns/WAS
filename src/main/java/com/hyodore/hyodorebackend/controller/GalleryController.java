package com.hyodore.hyodorebackend.controller;

import com.hyodore.hyodorebackend.dto.PresignedUrlResponse;
import com.hyodore.hyodorebackend.dto.UploadInitRequest;
import com.hyodore.hyodorebackend.service.S3PresignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gallery")
public class GalleryController {

  private final S3PresignedUrlService s3PresignedUrlService;

  @PostMapping("/upload/init")
  public ResponseEntity<PresignedUrlResponse> initUpload(
      @RequestBody UploadInitRequest uploadInitRequest
  ) {
    PresignedUrlResponse response = s3PresignedUrlService.generatePresignedUploadURL(
        uploadInitRequest.getFileName(),
        uploadInitRequest.getContentType());
    return ResponseEntity.ok(response);
  }
}
