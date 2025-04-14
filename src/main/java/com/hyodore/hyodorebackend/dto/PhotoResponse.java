package com.hyodore.hyodorebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoResponse {
    private String photoId;
    private String photoUrl;
}
