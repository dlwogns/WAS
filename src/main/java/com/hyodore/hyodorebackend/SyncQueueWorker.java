package com.hyodore.hyodorebackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyodore.hyodorebackend.dto.SyncQueueMessage;
import com.hyodore.hyodorebackend.dto.UploadResult;
import com.hyodore.hyodorebackend.service.PhotoService;
import com.hyodore.hyodorebackend.service.SyncQueueService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SyncQueueWorker {

  private final StringRedisTemplate redisTemplate;
  private final RedisScript<String> safePopScript;
  private final ObjectMapper objectMapper;
  private final PhotoService photoService;
  private final SyncQueueService syncQueueService;

  @PostConstruct
  public void runWorker() {
    Executors.newSingleThreadExecutor().submit(() -> {
      while (true) {
        String familyId = "123";
        //String queueKey = "family:" + familyId + ":syncQueue";
        String queueKey = "shared_gallery";
        String processingKey = "shared_gallery:processing";
        String json = redisTemplate.execute(
            safePopScript,
            List.of(queueKey, processingKey),
            "10" // 10초 TTL
        );

        if (json != null) {
          SyncQueueMessage msg = objectMapper.readValue(json, SyncQueueMessage.class);

          photoService.saveUploadedPhoto(msg.getPhotoId(), msg.getUserId(), familyId,
              msg.getPhotoUrl());

          syncQueueService.completeResult(msg.getRequestId(), new UploadResult(msg.getPhotoId()));
        } 
        Thread.sleep(10); // 부하 조절
      }
    });
  }

}
