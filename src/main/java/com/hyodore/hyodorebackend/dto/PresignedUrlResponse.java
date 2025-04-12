package com.hyodore.hyodorebackend.dto;

import lombok.Data;

@Data
public class PresignedUrlResponse {
    private String photoId;
    private String uploadUrl;
    private String photoUrl;
}
