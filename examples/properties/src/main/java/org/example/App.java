package org.example;

import io.jawisp.Jawisp;

public class App {

    void main() {
        Jawisp.build(config -> config
            .routes(route -> route
                .get("/", ctx -> ctx.text("Hello, world!!"))
            )).start();
    }
}
