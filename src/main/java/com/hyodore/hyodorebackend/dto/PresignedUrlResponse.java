package com.hyodore.hyodorebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresignedUrlResponse {
    private String photoId;
    private String uploadUrl;
    private String photoUrl;
}
