package com.hyodore.hyodorebackend.dto;

import com.hyodore.hyodorebackend.entity.Photo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SyncResult {
    private String syncedAt;
    private List<Photo> newPhoto;
    private List<Photo> deletedPhoto;
}
