package com.nhohantu.tcbookbe.common.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    /**
     * Uploads a file to MinIO and returns the path in format: bucket/filename
     *
     * @param file The file to upload
     * @return String path: warrior-news/uuid-originalfilename.ext
     */
    public String uploadFile(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            // Generate unique filename
            String fileName = UUID.randomUUID().toString() + extension;

            // Upload to MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            log.info("Successfully uploaded file {} as {} to bucket {}", originalFileName, fileName, bucket);

            // Return path as requested: bucket/filename
            return bucket + "/" + fileName;

        } catch (Exception e) {
            log.error("Error occurred while uploading file to MinIO: {}", e.getMessage());
            throw new RuntimeException("Could not upload file: " + e.getMessage());
        }
    }
}
