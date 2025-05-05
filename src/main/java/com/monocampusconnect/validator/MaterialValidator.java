package com.monocampusconnect.validator;

import com.monocampusconnect.dto.MaterialRequest;
import com.monocampusconnect.exception.ApiException;
import org.springframework.stereotype.Component;

@Component
public class MaterialValidator {

    public void validateMaterial(MaterialRequest request) {
        // Basic validation
        validateBasicFields(request);
        
        // File validation
        validateFile(request);
        
        // Course validation
        validateCourse(request);
    }

    private void validateBasicFields(MaterialRequest request) {
        if (request.getMaterialCode() == null || request.getMaterialCode().trim().isEmpty()) {
            throw new ApiException("Material code is required", 400);
        }

        if (request.getCourseCode() == null || request.getCourseCode().trim().isEmpty()) {
            throw new ApiException("Course code is required", 400);
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new ApiException("Material title is required", 400);
        }

        if (request.getType() == null || request.getType().trim().isEmpty()) {
            throw new ApiException("Material type is required", 400);
        }

        if (request.getUploadedBy() == null || request.getUploadedBy().trim().isEmpty()) {
            throw new ApiException("Uploaded by is required", 400);
        }
    }

    private void validateFile(MaterialRequest request) {
        if (request.getFileType() == null || request.getFileType().trim().isEmpty()) {
            throw new ApiException("File type is required", 400);
        }

        // Validate file type
        String fileType = request.getFileType().toLowerCase();
        if (!isValidFileType(fileType)) {
            throw new ApiException("Invalid file type. Allowed types: PDF, DOC, DOCX, PPT, PPTX, ZIP, JPG, PNG", 400);
        }

        if (request.getFileSize() > 50 * 1024 * 1024) { // 50MB limit
            throw new ApiException("File size cannot exceed 50MB", 400);
        }
    }

    private boolean isValidFileType(String fileType) {
        return fileType.endsWith("pdf") ||
               fileType.endsWith("doc") ||
               fileType.endsWith("docx") ||
               fileType.endsWith("ppt") ||
               fileType.endsWith("pptx") ||
               fileType.endsWith("zip") ||
               fileType.endsWith("jpg") ||
               fileType.endsWith("png");
    }

    private void validateCourse(MaterialRequest request) {
        if (request.getCourseCode().length() < 5 || request.getCourseCode().length() > 10) {
            throw new ApiException("Course code must be between 5 and 10 characters", 400);
        }

        if (!request.getCourseCode().matches("^[A-Z0-9]+$")) {
            throw new ApiException("Course code must contain only uppercase letters and numbers", 400);
        }
    }
}
