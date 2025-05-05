package com.monocampusconnect.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MaterialStats {
    private Long materialId;
    private String title;
    private String courseName;
    private String fileName;
    private LocalDateTime uploadDate;
    private Long downloadCount;
    private Double rating;
    private Long reviewCount;
    private String uploaderName;
}
