package com.hyodore.hyodorebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyodore.hyodorebackend.dto.SyncQueueMessage;
import com.hyodore.hyodorebackend.dto.SyncResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SyncQueueService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private final Map<String, CompletableFuture<SyncResult>> resultMap = new ConcurrentHashMap<>();

    public void waitForResult(String requestId, CompletableFuture<SyncResult> future) {
        resultMap.put(requestId, future);
    }

    public void completeResult(String requestId, SyncResult syncResult) {
        CompletableFuture<SyncResult> future = resultMap.remove(requestId);
        if(future != null) {future.complete(syncResult);}
    }

    public void enqueueSyncRequest(SyncQueueMessage message) {
        try {
            String queueKey = "family:"+message.getFamilyId()+":syncQueue";
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().leftPush(queueKey, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Queue 직렬화 실패", e);
        }
    }
}
