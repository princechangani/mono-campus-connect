package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.UserRoleCanonical;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleCanonicalRepository extends JpaRepository<UserRoleCanonical, Long> {
}

