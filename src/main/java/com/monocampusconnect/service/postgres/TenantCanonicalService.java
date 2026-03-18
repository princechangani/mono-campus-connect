package com.monocampusconnect.service.postgres;

import com.monocampusconnect.dto.canonical.postgres.TenantCanonicalRequest;
import com.monocampusconnect.dto.canonical.postgres.TenantCanonicalResponse;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.TenantCanonical;
import com.monocampusconnect.repository.postgres.TenantCanonicalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TenantCanonicalService {

    private final TenantCanonicalRepository tenantRepository;

    public TenantCanonicalService(TenantCanonicalRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    public TenantCanonicalResponse create(TenantCanonicalRequest request) {
        String normalizedSlug = request.getSlug().trim().toLowerCase();
        if (tenantRepository.existsBySlugAndIsDeletedFalse(normalizedSlug)) {
            throw new ApiException("Tenant slug already exists: " + normalizedSlug, 409);
        }

        TenantCanonical tenant = new TenantCanonical();
        tenant.setTenantPublicId(UUID.randomUUID());
        tenant.setName(request.getName().trim());
        tenant.setSlug(normalizedSlug);
        tenant.setDomain(request.getDomain());
        tenant.setPlan(request.getPlan() == null ? "basic" : request.getPlan());
        tenant.setTimezone(request.getTimezone() == null ? "UTC" : request.getTimezone());
        tenant.setLocale(request.getLocale() == null ? "en" : request.getLocale());
        tenant.setLogoUrl(request.getLogoUrl());
        tenant.setPrimaryColor(request.getPrimaryColor());
        tenant.setMaxStudents(request.getMaxStudents() == null ? 5000 : request.getMaxStudents());
        tenant.setMaxFaculty(request.getMaxFaculty() == null ? 500 : request.getMaxFaculty());
        tenant.setIsActive(request.getActive() == null ? Boolean.TRUE : request.getActive());
        tenant.setSubscriptionExpiresAt(request.getSubscriptionExpiresAt());
        tenant.setCreatedAt(OffsetDateTime.now());

        return toResponse(tenantRepository.save(tenant));
    }

    public List<TenantCanonicalResponse> getAll() {
        return tenantRepository.findByIsDeletedFalseOrderByCreatedAtDesc().stream().map(this::toResponse).toList();
    }

    public TenantCanonicalResponse getByPublicId(UUID tenantPublicId) {
        TenantCanonical tenant = tenantRepository.findByTenantPublicIdAndIsDeletedFalse(tenantPublicId)
                .orElseThrow(() -> new ApiException("Tenant not found", 404));
        return toResponse(tenant);
    }

    @Transactional
    public TenantCanonicalResponse update(UUID tenantPublicId, TenantCanonicalRequest request) {
        TenantCanonical tenant = tenantRepository.findByTenantPublicIdAndIsDeletedFalse(tenantPublicId)
                .orElseThrow(() -> new ApiException("Tenant not found", 404));

        String normalizedSlug = request.getSlug().trim().toLowerCase();
        if (!normalizedSlug.equals(tenant.getSlug()) && tenantRepository.existsBySlugAndIsDeletedFalse(normalizedSlug)) {
            throw new ApiException("Tenant slug already exists: " + normalizedSlug, 409);
        }

        tenant.setName(request.getName().trim());
        tenant.setSlug(normalizedSlug);
        tenant.setDomain(request.getDomain());
        tenant.setPlan(request.getPlan() == null ? tenant.getPlan() : request.getPlan());
        tenant.setTimezone(request.getTimezone() == null ? tenant.getTimezone() : request.getTimezone());
        tenant.setLocale(request.getLocale() == null ? tenant.getLocale() : request.getLocale());
        tenant.setLogoUrl(request.getLogoUrl());
        tenant.setPrimaryColor(request.getPrimaryColor());
        tenant.setMaxStudents(request.getMaxStudents() == null ? tenant.getMaxStudents() : request.getMaxStudents());
        tenant.setMaxFaculty(request.getMaxFaculty() == null ? tenant.getMaxFaculty() : request.getMaxFaculty());
        tenant.setIsActive(request.getActive() == null ? tenant.getIsActive() : request.getActive());
        tenant.setSubscriptionExpiresAt(request.getSubscriptionExpiresAt());
        tenant.setUpdatedAt(OffsetDateTime.now());

        return toResponse(tenantRepository.save(tenant));
    }

    @Transactional
    public void delete(UUID tenantPublicId) {
        TenantCanonical tenant = tenantRepository.findByTenantPublicIdAndIsDeletedFalse(tenantPublicId)
                .orElseThrow(() -> new ApiException("Tenant not found", 404));
        tenant.setIsDeleted(Boolean.TRUE);
        tenant.setDeletedAt(OffsetDateTime.now());
        tenantRepository.save(tenant);
    }

    private TenantCanonicalResponse toResponse(TenantCanonical tenant) {
        TenantCanonicalResponse response = new TenantCanonicalResponse();
        response.setTenantId(tenant.getTenantId());
        response.setTenantPublicId(tenant.getTenantPublicId());
        response.setName(tenant.getName());
        response.setSlug(tenant.getSlug());
        response.setDomain(tenant.getDomain());
        response.setPlan(tenant.getPlan());
        response.setTimezone(tenant.getTimezone());
        response.setLocale(tenant.getLocale());
        response.setLogoUrl(tenant.getLogoUrl());
        response.setPrimaryColor(tenant.getPrimaryColor());
        response.setMaxStudents(tenant.getMaxStudents());
        response.setMaxFaculty(tenant.getMaxFaculty());
        response.setActive(tenant.getIsActive());
        response.setSubscriptionExpiresAt(tenant.getSubscriptionExpiresAt());
        response.setCreatedAt(tenant.getCreatedAt());
        response.setUpdatedAt(tenant.getUpdatedAt());
        return response;
    }
}

