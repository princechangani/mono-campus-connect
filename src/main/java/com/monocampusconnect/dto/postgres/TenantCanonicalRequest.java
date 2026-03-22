package com.monocampusconnect.dto.postgres;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TenantCanonicalRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String slug;

    private String domain;
    private String plan;
    private String timezone;
    private String locale;
    private String logoUrl;
    private String primaryColor;

    @Min(1)
    @Max(1_000_000)
    private Integer maxStudents;

    @Min(1)
    @Max(100_000)
    private Integer maxFaculty;

    private Boolean active;
    private OffsetDateTime subscriptionExpiresAt;
}

