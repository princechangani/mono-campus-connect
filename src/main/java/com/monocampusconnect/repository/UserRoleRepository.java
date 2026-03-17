package com.monocampusconnect.repository;

import com.monocampusconnect.model.Role;
import com.monocampusconnect.model.User;
import com.monocampusconnect.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUser(User user);

    List<UserRole> findByRole(Role role);

    Optional<UserRole> findByUserAndRole(User user, Role role);

    boolean existsByUserAndRole(User user, Role role);

    void deleteByUserAndRole(User user, Role role);

    void deleteByUser(User user);

    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.role WHERE ur.user.id = :userId")
    List<UserRole> findByUserId(@Param("userId") Long userId);

    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.user WHERE ur.role.roleName = :roleName AND ur.user.tenantId = :tenantId")
    List<UserRole> findByRoleNameAndTenantId(
            @Param("roleName") Role.RoleName roleName,
            @Param("tenantId") java.util.UUID tenantId);
}

