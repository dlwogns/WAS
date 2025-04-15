package com.hyodore.hyodorebackend.dto;

import com.hyodore.hyodorebackend.entity.Photo;
import java.util.List;
import lombok.Data;

@Data
public class AllPhotosResponse {
  private List<Photo> photos;

  public AllPhotosResponse(List<Photo> photos) {
    this.photos = photos;
  }
}
