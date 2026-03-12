package org.example;

import io.jawisp.Jawisp;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    private static int TEST_PORT = 9876;
    private static Jawisp server;

    @BeforeAll
    public static void startServer() {
        server = Jawisp.build(config -> config
                .port(TEST_PORT)
                .routes(route -> route
                        .get("/", ctx -> ctx.text("Hello, world!"))));
        server.start();
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    @Test
    public void testRootEndpoint() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + TEST_PORT))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Hello, world!", response.body());
    }
}