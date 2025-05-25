package com.hyodore.hyodorebackend.controller;

import com.hyodore.hyodorebackend.dto.VideoUploadResponse;
import com.hyodore.hyodorebackend.service.FCMService;
import com.hyodore.hyodorebackend.service.S3Service;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/video")
public class VideoController {
  private final S3Service s3Service;
  private final FCMService FCMService;

  @PostMapping("/upload")
  public ResponseEntity<String> uploadVideo(
      @RequestParam("file") MultipartFile file,
      @RequestParam("userId") String userId,
      @RequestParam("familyId") String familyId
  ) {
    try {
      String videoUrl = s3Service.upload(file, "videos");
      System.out.println(videoUrl);
      // FCM 전송
      FCMService.sendNotification(
          "영상 전송",
          "이벤트 영상이 도착했습니다.",
          Map.of("videoUrl", videoUrl, "userId", userId, "familyId", familyId)
      );

      return ResponseEntity.ok("영상 전송 완료");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
