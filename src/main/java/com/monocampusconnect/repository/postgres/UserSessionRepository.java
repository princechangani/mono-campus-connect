package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
}

