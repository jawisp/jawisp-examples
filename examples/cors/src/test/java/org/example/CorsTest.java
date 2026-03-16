package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;

import io.jawisp.Jawisp;

public class CorsTest {

    @Test
    public void testUserEndpointPost() throws Exception {
        int testPort = 65001;
        
        Jawisp server = Jawisp.build(config -> config
                .port(testPort) // random port
                .cors(cors -> cors.origins("http://localhost:" + testPort))
                .routes(route -> route
                        .post("/api/v1/users", ctx -> {
                            var user = ctx.bodyAsClass(User.class);
                            ctx.status(201).json(ctx.jsonMapper().toJsonString(user, User.class));
                        })));
        server.start();

        HttpClient client = HttpClient.newHttpClient();
        String jsonBody = """
                {
                    "id": "1",
                    "name": "Test User",
                    "age": 33
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + testPort + "/api/v1/users"))
                .header("Content-Type", "application/json")
                .header("Origin", "http://localhost:" + testPort)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // CORS assertions
        assertEquals(201, response.statusCode());
        assertEquals("http://localhost:" + testPort,
                response.headers().firstValue("Access-Control-Allow-Origin").orElse(""));

        String body = response.body().trim().replaceAll("\\s+", " ");
        assertTrue(body.contains("Test User"), "Should echo name back");

        server.stop();
    }

    @Test
    public void testDisabledUserEndpointPost() throws Exception {
        int testPort = 65002;

        Jawisp server = Jawisp.build(config -> config
                .port(testPort) // random port
                .cors(cors -> cors.origins("http://localhost:65000"))
                .routes(route -> route
                        .post("/api/v1/users", ctx -> {
                            var user = ctx.bodyAsClass(User.class);
                            ctx.status(201).json(ctx.jsonMapper().toJsonString(user, User.class));
                        })));
        server.start();

        HttpClient client = HttpClient.newHttpClient();
        String jsonBody = """
                {
                    "id": "1",
                    "name": "Test User",
                    "age": 33
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + testPort + "/api/v1/users"))
                .header("Content-Type", "application/json")
                .header("Origin", "http://localhost:" + testPort)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // CORS assertions
        assertEquals(403, response.statusCode());

        server.stop();
    }

}