package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}

