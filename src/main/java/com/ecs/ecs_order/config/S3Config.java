package com.ecs.ecs_order.config;

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

    @Autowired
    private GoogleSecretManagerService secretManagerService;

    @Value("${secretName}")
    private String secretName;

    @Value("${secretVersion}")
    private String version;

    @Bean
    public S3Client s3Client() throws JsonProcessingException {
        try {
            String secretJson = secretManagerService.getSecret(secretName, version);
            System.out.println("Secret Json: " + secretJson);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode secretNode = objectMapper.readTree(secretJson);
            String accessKey = secretNode.get("accessKeyId").asText();
            String secretKey = secretNode.get("secretAccessKey").asText();
            String region = secretNode.get("s3Region").asText();
            System.out.println("Access Key ID: " + accessKey);
            System.out.println("Secret Access Key: " + secretKey);
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

