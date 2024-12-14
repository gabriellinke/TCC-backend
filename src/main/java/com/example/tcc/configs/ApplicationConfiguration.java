package com.example.tcc.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Value("${aws.s3.bucket.name}")
    private String bucketName;
    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;
    @Value("${ocr.server.endpoint}")
    private String ocrServerEndpoint;

    @Bean
    public String bucketName() {
        return bucketName;
    }

    @Bean
    public String bucketURL() {
        return "https://"+bucketName+".s3.us-east-1.amazonaws.com/";
    }

    @Bean
    public String jwtSecretKey() {
        return jwtSecretKey;
    }

    @Bean
    public Long jwtExpiration() {
        return jwtExpiration;
    }

    @Bean
    public String ocrServerEndpoint() {
        return ocrServerEndpoint;
    }

}