package com.hyodore.hyodorebackend.controller;

import com.hyodore.hyodorebackend.dto.PresignedUrlResponse;
import com.hyodore.hyodorebackend.dto.SyncQueueMessage;
import com.hyodore.hyodorebackend.dto.SyncResult;
import com.hyodore.hyodorebackend.dto.UploadCompleteRequest;
import com.hyodore.hyodorebackend.dto.UploadInitRequest;
import com.hyodore.hyodorebackend.service.S3PresignedUrlService;
import com.hyodore.hyodorebackend.service.SyncQueueService;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
  private final SyncQueueService syncQueueService;

  @PostMapping("/upload/init")
  public ResponseEntity<PresignedUrlResponse> initUpload(
      @RequestBody UploadInitRequest uploadInitRequest
  ) {
    PresignedUrlResponse response = s3PresignedUrlService.generatePresignedUploadURL(
        uploadInitRequest.getFileName(),
        uploadInitRequest.getContentType());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/upload/complete")
  public ResponseEntity<SyncResult> completeUpload(
      @RequestBody UploadCompleteRequest request
  ) throws Exception {
    String requestId = UUID.randomUUID().toString();
    CompletableFuture<SyncResult> future = new CompletableFuture<>();

    syncQueueService.waitForResult(requestId, future);

    SyncQueueMessage message = new SyncQueueMessage(
        "upload",
        request.getPhotoId(),
        request.getUserId(),
        "123",
        requestId
    );

    syncQueueService.enqueueSyncRequest(message);
    SyncResult result = future.get(5, TimeUnit.SECONDS);
    return ResponseEntity.ok(result);
  }
}
