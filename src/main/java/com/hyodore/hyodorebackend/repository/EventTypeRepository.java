package com.hyodore.hyodorebackend.repository;

import com.hyodore.hyodorebackend.entity.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTypeRepository extends JpaRepository<EventType, String> {

}
