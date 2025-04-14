package com.hyodore.hyodorebackend.repository;

import com.hyodore.hyodorebackend.entity.Device;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {

  List<Device> findByFamilyId(String familyId);
}
