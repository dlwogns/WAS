package com.hyodore.hyodorebackend.dto;

import java.util.List;
import lombok.Data;

@Data
public class DeleteRequest {
  private String userId;
  private List<String> photoIds;
}
