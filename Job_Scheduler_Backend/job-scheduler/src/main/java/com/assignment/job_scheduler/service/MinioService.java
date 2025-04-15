package com.assignment.job_scheduler.service;

import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

@Service
public class MinioService {

    private MinioClient minioClient;

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucket;

    @PostConstruct
    public void init() {
        minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();

        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MinIO bucket", e);
        }
    }

    // ✅ Upload file to MinIO
    public String uploadFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(fileName)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
            );

            return bucket + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    // ✅ Download file from MinIO and return a File object
    public File downloadFile(String objectName) {
    try {
        // Create a truly unique temp file name
        String uniqueName = System.currentTimeMillis() + "_" + objectName.replaceAll("/", "_");
        File tempFile = new File(System.getProperty("java.io.tmpdir"), uniqueName);

        // If file somehow exists (very rare), delete it first
        if (tempFile.exists()) {
            tempFile.delete();
        }

        // Download to the file
        minioClient.downloadObject(
            DownloadObjectArgs.builder()
                .bucket(bucket)
                .object(objectName)
                .filename(tempFile.getAbsolutePath())
                .build()
        );

        return tempFile;
    } catch (Exception e) {
        throw new RuntimeException("Failed to download file from MinIO", e);
    }
}

}
