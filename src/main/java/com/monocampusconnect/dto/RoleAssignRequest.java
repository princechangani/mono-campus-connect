package com.monocampusconnect.dto;

import java.util.List;

public class RoleAssignRequest {
    private Long userId;
    private List<String> roles;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}

