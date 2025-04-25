package com.hyodore.hyodorebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyodore.hyodorebackend.dto.SyncQueueMessage;
import com.hyodore.hyodorebackend.dto.UploadResult;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncQueueService {

  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;

  private final Map<String, CompletableFuture<UploadResult>> resultMap = new ConcurrentHashMap<>();

  public void waitForResult(String requestId, CompletableFuture<UploadResult> future) {
    resultMap.put(requestId, future);
  }

  public void completeResult(String requestId, UploadResult uploadResult) {
    CompletableFuture<UploadResult> future = resultMap.remove(requestId);
    if (future != null) {
      future.complete(uploadResult);
    }
  }

  public void enqueueSyncRequest(SyncQueueMessage message) {
    try {
      String queueKey = "shared_gallery";
      String json = objectMapper.writeValueAsString(message);
      redisTemplate.opsForList().leftPush(queueKey, json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Queue 직렬화 실패", e);
    }
  }
}
