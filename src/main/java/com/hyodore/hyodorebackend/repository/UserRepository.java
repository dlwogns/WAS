package com.hyodore.hyodorebackend.repository;

import com.hyodore.hyodorebackend.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  List<User> findByFamilyId(String familyId);

  Optional<User> findByUserId(String userId);
}
