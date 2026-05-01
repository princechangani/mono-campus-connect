package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
}

