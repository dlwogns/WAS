package com.hyodore.hyodorebackend.service;

import com.hyodore.hyodorebackend.dto.PresignedUrlResponse;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3PresignedUrlService {

  private final S3Presigner s3Presigner;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket;

  public PresignedUrlResponse generatePresignedUploadURL(String fileName, String contentType) {
    String extension = extractExtensionFromFileName(fileName);
    if (extension.isBlank()) {
      extension = extractExtensionFromContentType(contentType);
    }

    String photoId = UUID.randomUUID().toString();
    String key = "photos/" + photoId + "." + extension;

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType(contentType)
        .build();
    PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(5))
        .putObjectRequest(putObjectRequest)
        .build();

    URL presignedUrl = s3Presigner.presignPutObject(putObjectPresignRequest).url();

    return new PresignedUrlResponse(photoId, presignedUrl.toString(), buildPublicUrl(key));
  }

  private String buildPublicUrl(String key) {
    return "https://" + bucket + ".s3.amazonaws.com/" + key;
  }

  private String extractExtensionFromFileName(String fileName) {
    if (fileName == null || !fileName.contains(".")) {
      return "jpg"; // 기본 확장자
    }
    return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
  }

  private String extractExtensionFromContentType(String contentType) {
    if (contentType == null) {
      return "jpg";
    }
    return switch (contentType) {
      case "image/jpeg" -> "jpg";
      case "image/png" -> "png";
      case "image/gif" -> "gif";
      case "image/webp" -> "webp";
      default -> "jpg";
    };
  }

}
