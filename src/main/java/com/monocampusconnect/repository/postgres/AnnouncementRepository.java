package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}
