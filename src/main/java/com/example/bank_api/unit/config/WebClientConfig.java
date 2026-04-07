package com.example.bank_api.unit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder(
            @Value("${external.util-api.base-url}") String baseUrl) {

        return WebClient.builder()
                .baseUrl(baseUrl);
    }
}

