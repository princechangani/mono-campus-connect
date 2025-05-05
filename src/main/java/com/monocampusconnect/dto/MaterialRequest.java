package com.monocampusconnect.dto;

import lombok.Data;

@Data
public class MaterialRequest {
    private String materialCode;
    private String courseCode;
    private String title;
    private String description;
    private String type; // PDF, DOC, PPT, VIDEO, LINK
    private String url;
    private String uploadedBy;
    private long fileSize;
    private String fileType;


}
