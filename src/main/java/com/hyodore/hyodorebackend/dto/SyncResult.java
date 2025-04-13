package com.hyodore.hyodorebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SyncResult {
    private String syncedAt;
    private List<PhotoResponse> newPhoto;
    private List<String> deletedPhoto;
}
