package org.example;

import java.util.Map;

import io.jawisp.Jawisp;
import io.jawisp.http.Context;

public class App {

    static void homePage(Context ctx) {
        ctx.render("home.html", Map.of(
                "name", "John Smith",
                "title", "Jawisp Example"));
    }

    void main() {
        Jawisp.build(config -> config
                .templateEngine("pebble")
                .staticResources("/static")
                .routes(route -> route
                        .get("/", App::homePage)))
                .start();
    }
}
