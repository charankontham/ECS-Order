package com.ecs.ecs_order.config;

import com.ecs.ecs_order.util.ExtractSecrets;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client(){
        try {
            String accessKey = ExtractSecrets.getSecret("ACCESS_KEY_ID");
            String secretKey = ExtractSecrets.getSecret("SECRET_ACCESS_KEY");
            String region = ExtractSecrets.getSecret("S3_REGION");
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
            return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region))
                .build();

        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            throw e;
        }
    }
}

