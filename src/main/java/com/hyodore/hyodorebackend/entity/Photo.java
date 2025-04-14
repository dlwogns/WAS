package com.hyodore.hyodorebackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "photo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Photo {

  @Id
  @Column(name = "photo_id")
  private String photoId;

  @Column(name = "family_id", nullable = false)
  private String familyId;

  @Column(name = "photo_url", columnDefinition = "TEXT")
  private String photoUrl;

  @Column(name = "uploaded_by", length = 20)
  private String uploadedBy;

  @Column(name = "uploaded_at")
  private LocalDateTime uploadedAt;

  @Column(name = "deleted")
  private boolean deleted;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;
}

