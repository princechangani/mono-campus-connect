package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "materials")
@Data
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String materialCode;
    private String courseCode;
    private String title;
    private String description;
    private String type; // PDF, DOC, PPT, VIDEO, LINK
    
    @Lob
    private byte[] fileContent;
    
    private String uploadedBy;
    private Date uploadedDate;
    private long fileSize;
    
    private int downloadCount;
    
    private Date lastUpdatedDate;
    private String fileType;
}
