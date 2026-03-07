package com.monocampusconnect.config;

import java.util.UUID;

/**
 * Holds the current tenant ID in a ThreadLocal variable.
 * Set by TenantFilter on each request, cleared after response.
 */
public class TenantContextHolder {

    private static final ThreadLocal<UUID> TENANT_CONTEXT = new ThreadLocal<>();

    private TenantContextHolder() {}

    public static void setTenantId(UUID tenantId) {
        TENANT_CONTEXT.set(tenantId);
    }

    public static UUID getTenantId() {
        return TENANT_CONTEXT.get();
    }

    public static void clear() {
        TENANT_CONTEXT.remove();
    }
}

