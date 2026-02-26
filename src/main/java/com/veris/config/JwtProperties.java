package com.veris.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "veris.jwt")
public class JwtProperties {
    private String secret = "default_secret";
    private long expiration = 3600;
}
