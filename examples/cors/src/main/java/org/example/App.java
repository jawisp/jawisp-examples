package org.example;

import java.util.Map;

import io.jawisp.Jawisp;
import io.jawisp.http.Context;

public class App {

    static void createUser(Context ctx) {
        var user = ctx.bodyAsClass(User.class);
        ctx.status(201).json(ctx.jsonMapper().toJsonString(user, User.class));
    }

    static void getUser(Context ctx) {
        var id = ctx.pathParam("id");
        ctx.status(200).json("{\"userId\":\"" + id + "\", \"name\":\"Taras\"}");
    }

    static void homePage(Context ctx) {
        ctx.render("home.html", Map.of(
                "name", "John Smith",
                "title", "Jawisp Example"));
    }

    void main() {
        Jawisp.build(config -> config
                .templateEngine("pebble")
                .staticResources("/static")
                .cors(cors -> cors.origins("http://localhost:8080"))
                .routes(route -> route
                        .get("/", App::homePage)
                        .get("/api/v1/users/:id", App::getUser)
                        .post("/api/v1/users", App::createUser)
                    ))
                .start();
    }
}
