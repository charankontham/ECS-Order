package com.ecs.ecs_order.config;
import com.google.cloud.secretmanager.v1.AccessSecretVersionRequest;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretPayload;
import org.springframework.stereotype.Service;

@Service
public class GoogleSecretManagerService {

    public String getSecret(String secretName, String version) {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            String secretVersionName = String.format("projects/%s/secrets/%s/versions/%s",
                    "gentle-bot-445100-b3",
                    secretName,
                    version);

            AccessSecretVersionRequest request = AccessSecretVersionRequest.newBuilder()
                    .setName(secretVersionName)
                    .build();
            SecretPayload payload = client.accessSecretVersion(request).getPayload();
            return payload.getData().toStringUtf8();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
