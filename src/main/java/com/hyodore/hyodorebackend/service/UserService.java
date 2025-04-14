package com.hyodore.hyodorebackend.service;

import com.hyodore.hyodorebackend.entity.User;
import com.hyodore.hyodorebackend.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public String getFamilyIdByUserId(String userId) {
    return userRepository.findById(userId)
        .map(User::getFamilyId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + userId));
  }

  public User getUserById(String userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + userId));
  }

  public List<User> getUsersByFamilyId(String familyId) {
    return userRepository.findByFamilyId(familyId);
  }
}
