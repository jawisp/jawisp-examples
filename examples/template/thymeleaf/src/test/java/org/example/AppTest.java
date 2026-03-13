package org.example;

import io.jawisp.Jawisp;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    private static int testPort;
    private static Jawisp server;

    @BeforeAll
    public static void startServer() {
        server = Jawisp.build(config -> config
                .port(0)
                .templateEngine("thymeleaf")
                .staticResources("/static")
                .routes(route -> route
                        .get("/", ctx -> ctx.render("home.html", Map.of(
                                "name", "John Smith",
                                "title", "Jawisp Example")))));
        server.start();

        // Get the actual port Jawisp bound to
        testPort = server.config().port();

    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    @Test
    public void testRootEndpoint() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + testPort))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body().trim().replaceAll("\\s+", " ");
        assertTrue(body.contains("<title>Jawisp Example</title>"), "Should have correct title");
        assertTrue(body.contains("<h1>Home page</h1>"), "Should have home page heading");
        assertTrue(body.contains("Welcome to my home page"), "Should have welcome message");
        assertTrue(body.contains("reset.css"), "Should link to CSS");
    }
}