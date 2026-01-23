package com.nhohantu.tcbookbe.common.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadService {

    private final Cloudinary cloudinary;

    /**
     * Uploads a file to Cloudinary and returns the full secure URL
     *
     * @param file The file to upload
     * @return String full secure URL
     */
    public String uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));

            String url = (String) uploadResult.get("secure_url");
            log.info("Successfully uploaded file to Cloudinary. URL: {}", url);

            return url;
        } catch (IOException e) {
            log.error("Error occurred while uploading file to Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Could not upload file: " + e.getMessage());
        }
    }

    /**
     * Deletes a file from Cloudinary given its secure URL
     *
     * @param url The full secure URL of the file
     */
    public void deleteFile(String url) {
        if (url == null || url.isEmpty() || !url.contains("cloudinary.com")) {
            return;
        }

        try {
            // Extract public_id from URL
            // Format:
            // https://res.cloudinary.com/cloud_name/image/upload/v12345678/public_id.jpg
            String publicId = extractPublicId(url);
            if (publicId != null) {
                Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                log.info("Deleted file from Cloudinary. Public ID: {}, Result: {}", publicId, result);
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting file from Cloudinary: {}", e.getMessage());
        }
    }

    private String extractPublicId(String url) {
        try {
            String splitPattern = "/upload/";
            if (!url.contains(splitPattern))
                return null;

            String afterUpload = url.substring(url.lastIndexOf(splitPattern) + splitPattern.length());

            // Remove version if exists (e.g., v162345678/)
            if (afterUpload.startsWith("v")) {
                int firstSlash = afterUpload.indexOf("/");
                if (firstSlash != -1) {
                    afterUpload = afterUpload.substring(firstSlash + 1);
                }
            }

            // Remove extension
            int lastDot = afterUpload.lastIndexOf(".");
            if (lastDot != -1) {
                return afterUpload.substring(0, lastDot);
            }
            return afterUpload;
        } catch (Exception e) {
            log.error("Could not extract public_id from URL: {}", url);
        }
        return null;
    }
}
