package com.monocampusconnect.repository;

import com.monocampusconnect.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(Long id);
    List<Event> findByPostedBy(String postedBy);
    List<Event> findByTenantId(UUID tenantId);
    long countByTenantId(UUID tenantId);
}
