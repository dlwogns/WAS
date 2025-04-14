package com.hyodore.hyodorebackend.repository;

import com.hyodore.hyodorebackend.entity.Photo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {

  List<Photo> findByFamilyIdAndDeletedFalseAndUploadedAtAfter(String familyId, LocalDateTime since);

  List<Photo> findByFamilyIdAndDeletedTrueAndDeletedAtAfter(String familyId, LocalDateTime since);

  List<Photo> findByUploadedBy(String userId);
}
