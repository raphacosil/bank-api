package com.example.bank_api.infra.gateway;

import com.example.bank_api.infra.gateway.dto.AuthorizeResponse;
import com.example.bank_api.infra.gateway.dto.SendNotificationResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ApiGateway {

    private final WebClient webClient;

    public ApiGateway(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public AuthorizeResponse authorize() {
        return webClient.get()
                .uri("/v2/authorize")
                .retrieve()
                .bodyToMono(AuthorizeResponse.class)
                .block();
    }

    public SendNotificationResponse sendNotification() {
        return webClient.post()
                .uri("/v1/notify")
                .retrieve()
                .bodyToMono(SendNotificationResponse.class)
                .block();
    }
}