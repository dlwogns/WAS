package com.hyodore.hyodorebackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

  @Id
  @Column(name = "schedule_id")
  private String scheduleId;

  @Column(name = "family_id", nullable = false, length = 20)
  private String familyId;

  @Column(name = "user_id", length = 20)
  private String userId;

  @Column(name = "schedule_desc", length = 100)
  private String scheduleDesc;

  @Column(name = "schedule_date", nullable = false)
  private OffsetDateTime scheduleDate;
}
