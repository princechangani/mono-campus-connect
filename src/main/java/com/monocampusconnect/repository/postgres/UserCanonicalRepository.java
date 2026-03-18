package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.UserCanonical;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCanonicalRepository extends JpaRepository<UserCanonical, Long> {
}

