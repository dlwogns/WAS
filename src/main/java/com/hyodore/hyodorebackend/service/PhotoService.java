package com.hyodore.hyodorebackend.service;

import com.hyodore.hyodorebackend.entity.Photo;
import com.hyodore.hyodorebackend.repository.PhotoRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhotoService {

  private final PhotoRepository photoRepository;
  private final UserService userService;

  public List<Photo> findNewPhotosSince(String userId, LocalDateTime since) {
    String familyId = userService.getFamilyIdByUserId(userId);
    return photoRepository.findByFamilyIdAndDeletedFalseAndUploadedAtAfter(familyId, since);
  }

  public List<Photo> findDeletedPhotosSince(String userId, LocalDateTime since) {
    String familyId = userService.getFamilyIdByUserId(userId);
    return photoRepository.findByFamilyIdAndDeletedTrueAndDeletedAtAfter(familyId, since);
  }

  public void saveUploadedPhoto(String photoId, String userId, String familyId, String photoUrl) {
    Photo photo = Photo.builder()
        .photoId(photoId)
        .uploadedBy(userId)
        .familyId(familyId)
        .photoUrl(photoUrl)
        .uploadedAt(LocalDateTime.now())
        .deleted(false)
        .build();

    photoRepository.save(photo);
  }

  @Transactional
  public void softDeletePhotos(List<String> photoIds) {
    List<Photo> photos = photoRepository.findAllById(photoIds);
    for (Photo photo : photos) {
      photo.setDeleted(true);
      photo.setDeletedAt(LocalDateTime.now());
    }

    photoRepository.saveAll(photos);
  }
}
