package com.monocampusconnect.service;

import com.monocampusconnect.dto.TenantRequest;
import com.monocampusconnect.dto.TenantResponse;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Role;
import com.monocampusconnect.model.Tenant;
import com.monocampusconnect.model.User;
import com.monocampusconnect.repository.TenantRepository;
import com.monocampusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    /**
     * Onboard a new college tenant and create its first ADMIN user.
     */
    @Transactional
    public TenantResponse createTenant(TenantRequest request) {
        if (tenantRepository.existsByCode(request.getCode().toUpperCase())) {
            throw new ApiException("College code already in use: " + request.getCode(), 409);
        }
        if (tenantRepository.existsByName(request.getName())) {
            throw new ApiException("College name already registered: " + request.getName(), 409);
        }

        Tenant tenant = new Tenant();
        tenant.setName(request.getName());
        tenant.setCode(request.getCode().toUpperCase());
        tenant.setContactEmail(request.getContactEmail());
        tenant.setContactPhone(request.getContactPhone());
        tenant.setAddress(request.getAddress());
        tenant.setTimezone(request.getTimezone() != null ? request.getTimezone() : "Asia/Kolkata");
        tenant.setLogoUrl(request.getLogoUrl());
        tenant.setSubscriptionPlan(
                request.getSubscriptionPlan() != null ? request.getSubscriptionPlan() : Tenant.SubscriptionPlan.BASIC);
        tenant.setEnabled(true);
        Tenant saved = tenantRepository.save(tenant);

        User admin = new User();
        admin.setTenantId(saved.getTenantId());
        admin.setEmail(request.getContactEmail());
        admin.setPassword(passwordEncoder.encode(request.getAdminPassword()));
        admin.setFirstName(request.getAdminFirstName());
        admin.setLastName(request.getAdminLastName());
        admin.setEnabled(true);
        admin.setCreatedAt(new Date());
        admin.setUpdatedAt(new Date());
        User savedAdmin = userRepository.save(admin);
        roleService.assignInitialRole(savedAdmin, Role.RoleName.ADMIN);

        return TenantResponse.from(saved);
    }

    public List<TenantResponse> getAllTenants() {
        return tenantRepository.findAll()
                .stream()
                .map(TenantResponse::from)
                .collect(Collectors.toList());
    }

    public TenantResponse getTenantById(UUID id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ApiException("Tenant not found", 404));
        return TenantResponse.from(tenant);
    }

    @Transactional
    public TenantResponse updateTenant(UUID id, TenantRequest request) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ApiException("Tenant not found", 404));

        tenant.setName(request.getName());
        tenant.setContactEmail(request.getContactEmail());
        tenant.setContactPhone(request.getContactPhone());
        tenant.setAddress(request.getAddress());
        if (request.getTimezone() != null) tenant.setTimezone(request.getTimezone());
        if (request.getLogoUrl() != null) tenant.setLogoUrl(request.getLogoUrl());
        if (request.getSubscriptionPlan() != null) tenant.setSubscriptionPlan(request.getSubscriptionPlan());

        return TenantResponse.from(tenantRepository.save(tenant));
    }

    @Transactional
    public TenantResponse setTenantEnabled(UUID id, boolean enabled) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ApiException("Tenant not found", 404));
        tenant.setEnabled(enabled);
        return TenantResponse.from(tenantRepository.save(tenant));
    }
}
