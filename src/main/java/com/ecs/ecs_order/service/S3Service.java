package com.ecs.ecs_order.service;

import com.ecs.ecs_order.util.ExtractSecrets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.file.Paths;

@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(File file, String key) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(ExtractSecrets.getSecret("S3_BUCKET_NAME"))
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                ExtractSecrets.getSecret("S3_BUCKET_NAME"),
                ExtractSecrets.getSecret("S3_REGION"),
                key);
    }

    public Object downloadFile(String key, String downloadPath) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(ExtractSecrets.getSecret("S3_BUCKET_NAME"))
                    .key(key)
                    .build();

            s3Client.getObject(getObjectRequest, Paths.get(downloadPath));
            return new File(downloadPath);
        } catch (NoSuchKeyException e) {
            return HttpStatus.NOT_FOUND;
        }
    }
}
