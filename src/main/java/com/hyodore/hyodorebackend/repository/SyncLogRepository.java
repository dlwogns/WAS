package com.hyodore.hyodorebackend.repository;


import com.hyodore.hyodorebackend.entity.SyncLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncLogRepository extends JpaRepository<SyncLog, Long> {

  Optional<SyncLog> findTopByFamilyIdOrderByLastSyncedDesc(String familyId);

  Optional<SyncLog> findTopByDeviceIdOrderByLastSyncedDesc(String deviceId);
}

