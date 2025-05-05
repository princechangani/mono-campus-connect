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
    private String url; // For links and uploaded files
    private String uploadedBy;
    private Date uploadedDate;
    private long fileSize;
    public long getFileSize() {
        return fileSize;
    }

    private int downloadCount;
    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    private Date lastUpdatedDate;
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    private String fileType;
}
