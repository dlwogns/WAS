package com.hyodore.hyodorebackend.repository;

import com.hyodore.hyodorebackend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
