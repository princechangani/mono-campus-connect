package com.monocampusconnect.dto;

import com.monocampusconnect.model.Tenant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TenantRequest {

    @NotBlank(message = "College name is required")
    private String name;

    @NotBlank(message = "College code is required")
    private String code;

    @Email(message = "Valid contact email is required")
    @NotBlank(message = "Contact email is required")
    private String contactEmail;

    private String contactPhone;
    private String address;
    private String timezone;
    private String logoUrl;
    private Tenant.SubscriptionPlan subscriptionPlan;

    // Admin user details (created automatically on tenant creation)
    @NotBlank(message = "Admin first name is required")
    private String adminFirstName;

    @NotBlank(message = "Admin last name is required")
    private String adminLastName;

    @NotBlank(message = "Admin password is required")
    private String adminPassword;
}

