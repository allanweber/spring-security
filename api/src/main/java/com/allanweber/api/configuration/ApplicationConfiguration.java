package com.allanweber.api.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Configuration
@NoArgsConstructor
@ConfigurationProperties("application")
@Data
public class ApplicationConfiguration {

    @NotBlank
    private String verificationUrl;
}
