package com.monocampusconnect.repository;

import com.monocampusconnect.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(Role.RoleName code);
    boolean existsByCode(Role.RoleName code);
}
