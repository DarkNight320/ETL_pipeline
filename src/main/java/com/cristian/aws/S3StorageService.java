package com.cristian.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class S3StorageService {
    private static final Logger log = LoggerFactory.getLogger(S3StorageService.class);

    private final S3Client s3;

    public S3StorageService(Region region) {
        this.s3 = S3Client.builder()
                .region(region)
                .build();
    }

    public void uploadFile(String bucket, String key, Path file) throws IOException {
        if (!Files.exists(file)) {
            throw new IOException("File does not exist: " + file);
        }
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3.putObject(request, RequestBody.fromFile(file));
            log.info("Uploaded {} to s3://{}/{}", file, bucket, key);
        } catch (S3Exception e) {
            log.error("S3 putObject failed: {}", e.awsErrorDetails().errorMessage());
            throw e;
        }
    }

    public void downloadFile(String bucket, String key, Path destination) throws IOException {
        try {
            GetObjectRequest req = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3.getObject(req, destination);
            log.info("Downloaded s3://{}/{} to {}", bucket, key, destination);
        } catch (S3Exception e) {
            log.error("S3 getObject failed: {}", e.awsErrorDetails().errorMessage());
            throw e;
        }
    }

    public boolean bucketExists(String bucket) {
        try {
            HeadBucketRequest req = HeadBucketRequest.builder().bucket(bucket).build();
            s3.headBucket(req);
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        } catch (S3Exception e) {
            log.warn("HeadBucket failed: {}", e.awsErrorDetails().errorMessage());
            return false;
        }
    }
}
