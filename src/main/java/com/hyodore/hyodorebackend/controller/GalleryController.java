package com.hyodore.hyodorebackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyodore.hyodorebackend.dto.AllPhotosResponse;
import com.hyodore.hyodorebackend.dto.DeleteRequest;
import com.hyodore.hyodorebackend.dto.PhotoInfo;
import com.hyodore.hyodorebackend.dto.PresignedUrlResponse;
import com.hyodore.hyodorebackend.dto.SyncQueueMessage;
import com.hyodore.hyodorebackend.dto.SyncResult;
import com.hyodore.hyodorebackend.dto.UploadCompleteRequest;
import com.hyodore.hyodorebackend.dto.UploadInitRequest;
import com.hyodore.hyodorebackend.dto.UploadResult;
import com.hyodore.hyodorebackend.entity.Photo;
import com.hyodore.hyodorebackend.service.MqttPublisherService;
import com.hyodore.hyodorebackend.service.PhotoService;
import com.hyodore.hyodorebackend.service.S3Service;
import com.hyodore.hyodorebackend.service.SyncLogService;
import com.hyodore.hyodorebackend.service.SyncQueueService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gallery")
public class GalleryController {

  private final S3Service s3Service;
  private final SyncQueueService syncQueueService;
  private final PhotoService photoService;
  private final SyncLogService syncLogService;
  private final MqttPublisherService mqttPublisherService;
  private final ObjectMapper objectMapper;

  @PostMapping("/upload/init")
  public ResponseEntity<List<PresignedUrlResponse>> initUpload(
      @RequestBody List<UploadInitRequest> requests
  ) {
    List<PresignedUrlResponse> responses = requests.stream()
        .map(req -> s3Service.generatePresignedUploadURL(
            req.getFileName(), req.getContentType()))
        .toList();
    return ResponseEntity.ok(responses);
  }

  @PostMapping("/upload/complete")
  public ResponseEntity<SyncResult> completeUpload(
      @RequestBody UploadCompleteRequest requests
  ) throws Exception {
    String userId = requests.getUserId();

    LocalDateTime lastSyncedAt = syncLogService.findLastSyncedAtByUserId(userId);

    List<CompletableFuture<UploadResult>> futures = new ArrayList<>(); // 결과값 기다리기
    List<SyncQueueMessage> messages = new ArrayList<>(); // Queue에 넣을 message

    for (PhotoInfo photoInfo : requests.getPhotos()) {
      String requestId = UUID.randomUUID().toString();
      CompletableFuture<UploadResult> future = new CompletableFuture<>();

      syncQueueService.waitForResult(requestId, future);

      SyncQueueMessage message = new SyncQueueMessage(
          "upload",
          photoInfo.getPhotoId(),
          photoInfo.getPhotoUrl(),
          userId,
          "123",
          requestId
      );

      messages.add(message);
      futures.add(future);
    }
    for (SyncQueueMessage message : messages) {
      syncQueueService.enqueueSyncRequest(message);
    }

    List<UploadResult> uploadResults = new ArrayList<>();
    for (CompletableFuture<UploadResult> future : futures) {
      uploadResults.add(future.get(30, TimeUnit.SECONDS));
    }

    List<Photo> newPhotos = photoService.findNewPhotosSince(userId, lastSyncedAt);
    List<Photo> deletedPhotos = photoService.findDeletedPhotosSince(userId, lastSyncedAt);

    LocalDateTime now = LocalDateTime.now();
    syncLogService.saveSyncLog(userId, now);

    SyncResult ret = new SyncResult(now.toString(), newPhotos, deletedPhotos);

    mqttPublisherService.publish("gallery", objectMapper.writeValueAsString(ret));

    return ResponseEntity.ok(ret);
  }

  @PostMapping("/delete")
  public ResponseEntity<SyncResult> deletePhotos(
      @RequestBody DeleteRequest request
  ) throws JsonProcessingException {
    String userId = request.getUserId();
    List<String> photoIds = request.getPhotoIds();

    photoService.softDeletePhotos(photoIds);

    LocalDateTime lastSyncedAt = syncLogService.findLastSyncedAtByUserId(userId);

    List<Photo> newPhotos = photoService.findNewPhotosSince(userId, lastSyncedAt);
    List<Photo> deletedPhotos = photoService.findDeletedPhotosSince(userId, lastSyncedAt);

    LocalDateTime now = LocalDateTime.now();
    syncLogService.saveSyncLog(userId, now);

    SyncResult ret = new SyncResult(now.toString(), newPhotos, deletedPhotos);

    mqttPublisherService.publish("gallery", objectMapper.writeValueAsString(ret));

    return ResponseEntity.ok(ret);
  }

  @GetMapping("/sync")
  public ResponseEntity<SyncResult> getChangedPhotos(@RequestParam String userId) {
    LocalDateTime lastSyncedAt = syncLogService.findLastSyncedAtByUserId(userId);

    List<Photo> newPhotos = photoService.findNewPhotosSince(userId, lastSyncedAt);
    List<Photo> deletedPhotos = photoService.findDeletedPhotosSince(userId, lastSyncedAt);

    LocalDateTime now = LocalDateTime.now();
    syncLogService.saveSyncLog(userId, now);

    return ResponseEntity.ok(new SyncResult(now.toString(), newPhotos, deletedPhotos));
  }

  @GetMapping("/all")
  public ResponseEntity<AllPhotosResponse> getAllPhotos(@RequestParam String userId) {
    List<Photo> photos = photoService.findAllPhotos(userId);
    return ResponseEntity.ok(new AllPhotosResponse(photos));
  }
}
