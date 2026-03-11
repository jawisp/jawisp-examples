package org.example;

import io.jawisp.Jawisp;

public class App {

    public static void main(String[] args) {
        Jawisp.build(config -> config
            .routes(route -> route
                .get("/", ctx -> ctx.text("Hello, world!"))
            )).start();
    }
}
