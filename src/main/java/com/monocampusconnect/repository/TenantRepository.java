package com.monocampusconnect.repository;

import com.monocampusconnect.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    Optional<Tenant> findByCode(String code);
    Optional<Tenant> findByName(String name);
    boolean existsByCode(String code);
    boolean existsByName(String name);
}

