package com.example.bank_api.unit.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

class WebClientConfigTest {

    private final WebClientConfig config = new WebClientConfig();

    @Test
    void shouldCreateWebClientBuilderWithBaseUrl() {
        String baseUrl = "https://example.com";

        WebClient.Builder builder = config.webClientBuilder(baseUrl);
        WebClient webClient = builder.build();

        assertThat(webClient).isNotNull();
    }
}

