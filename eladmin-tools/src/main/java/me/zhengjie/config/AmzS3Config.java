package me.zhengjie.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@ApplicationScoped
public class AmzS3Config {

    @Inject
    AmzS3ConfigProperty amzS3ConfigProperty;

    @Produces
    @ApplicationScoped
    public S3Client amazonS3Client() {
        return S3Client.builder()
                .region(Region.of(amzS3ConfigProperty.region()))
                .endpointOverride(URI.create(amzS3ConfigProperty.endPoint()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(amzS3ConfigProperty.accessKey(), amzS3ConfigProperty.secretKey())
                        )
                )
                .build();
    }
}
