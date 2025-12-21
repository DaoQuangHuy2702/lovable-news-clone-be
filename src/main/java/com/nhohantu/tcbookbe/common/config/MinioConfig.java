package com.nhohantu.tcbookbe.common.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucket;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    @PostConstruct
    public void init() {
        try {
            // Build a temporary client for initialization to avoid circular reference
            // issues
            MinioClient client = MinioClient.builder()
                    .endpoint(url)
                    .credentials(accessKey, secretKey)
                    .build();

            boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                log.info("Creating bucket: {}", bucket);
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());

                // Set public read-only policy
                String policy = "{\n" +
                        "    \"Version\": \"2012-10-17\",\n" +
                        "    \"Statement\": [\n" +
                        "        {\n" +
                        "            \"Action\": [\n" +
                        "                \"s3:GetObject\"\n" +
                        "            ],\n" +
                        "            \"Effect\": \"Allow\",\n" +
                        "            \"Principal\": {\n" +
                        "                \"AWS\": [\n" +
                        "                    \"*\"\n" +
                        "                ]\n" +
                        "            },\n" +
                        "            \"Resource\": [\n" +
                        "                \"arn:aws:s3:::" + bucket + "/*\"\n" +
                        "            ],\n" +
                        "            \"Sid\": \"\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";

                client.setBucketPolicy(
                        SetBucketPolicyArgs.builder()
                                .bucket(bucket)
                                .config(policy)
                                .build());
                log.info("Bucket {} created and policy set to public read-only", bucket);
            } else {
                log.info("Bucket {} already exists", bucket);
            }
        } catch (Exception e) {
            log.error("Error initializing MinIO bucket: {}", e.getMessage());
        }
    }
}
