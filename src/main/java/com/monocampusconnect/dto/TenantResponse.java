package com.monocampusconnect.dto;

import com.monocampusconnect.model.Tenant;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class TenantResponse {
    private UUID id;
    private String name;
    private String code;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private String timezone;
    private String logoUrl;
    private Tenant.SubscriptionPlan subscriptionPlan;
    private boolean enabled;
    private Date createdAt;
    // Stats
    private long totalUsers;
    private long totalStudents;
    private long totalFaculty;

    public static TenantResponse from(Tenant tenant) {
        TenantResponse r = new TenantResponse();
        r.setId(tenant.getTenantId());
        r.setName(tenant.getName());
        r.setCode(tenant.getCode());
        r.setContactEmail(tenant.getContactEmail());
        r.setContactPhone(tenant.getContactPhone());
        r.setAddress(tenant.getAddress());
        r.setTimezone(tenant.getTimezone());
        r.setLogoUrl(tenant.getLogoUrl());
        r.setSubscriptionPlan(tenant.getSubscriptionPlan());
        r.setEnabled(tenant.isEnabled());
        r.setCreatedAt(tenant.getCreatedAt());
        return r;
    }
}

