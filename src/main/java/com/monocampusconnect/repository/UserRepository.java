package com.monocampusconnect.repository;

import com.monocampusconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndTenantId(String email, UUID tenantId);
    List<User> findByTenantId(UUID tenantId);
    boolean existsByEmailAndTenantId(String email, UUID tenantId);
    long countByTenantId(UUID tenantId);
    Optional<User> findByUsersPublicIdAndTenantId(UUID usersPublicId, UUID tenantId);
}
