package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.RoleCanonical;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleCanonicalRepository extends JpaRepository<RoleCanonical, Long> {
}

