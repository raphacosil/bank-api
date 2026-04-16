package com.example.bank_api.unit.gateway;

import com.example.bank_api.infra.gateway.ApiGateway;
import com.example.bank_api.infra.gateway.dto.AuthorizeResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApiGatewayTest {

    private MockWebServer mockWebServer;
    private ApiGateway apiGateway;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        apiGateway = new ApiGateway(webClient.mutate());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void whenAuthorize_shouldReturnSuccess() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("""
                            {
                              "status": "success",
                              "data": {
                                "authorization": true
                              }
                            }
                        """)
                        .addHeader("Content-Type", "application/json")
        );

        AuthorizeResponse response = apiGateway.authorize();

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertTrue(response.getData().getAuthorization());
    }

    @Test
    void whenAuthorize_shouldReturnFail() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(403)
                        .setBody("""
                            {
                              "status": "fail",
                              "data": {
                                "authorization": false
                              }
                            }
                        """)
                        .addHeader("Content-Type", "application/json")
        );
        WebClientResponseException exception =
                assertThrows(WebClientResponseException.Forbidden.class,
                        () -> apiGateway.authorize()
                );

        assertEquals(HttpStatusCode.valueOf(403), exception.getStatusCode());
    }

    @Test
    void whenSendNotification_shouldReturnSuccess() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(204)
        );

        AuthorizeResponse response = apiGateway.authorize();

        assertNull(response);
    }

    @Test
    void whenSendNotification_shouldReturnError() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(504)
                        .setBody("""
                            {
                                "status": "error",
                                "message": "The service is not available, try again later"
                            }
                        """)
        );
        WebClientResponseException exception =
                assertThrows(WebClientResponseException.GatewayTimeout.class,
                        () -> apiGateway.sendNotification()
                );

        assertEquals(HttpStatusCode.valueOf(504), exception.getStatusCode());
    }
}

