package com.zacharyab24.playground.os;

import io.restassured.RestAssured;
import jakarta.ws.rs.SeBootstrap;
import jakarta.ws.rs.core.Application;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.Set;
import java.util.concurrent.CompletionStage;

public abstract class BaseResourceTest {

    private static SeBootstrap.Instance server;

    protected abstract Set<Class<?>> getResourceClasses();

    @BeforeAll
    void startServer() throws Exception {
        Application app = new Application() {
            @Override
            public Set<Class<?>> getClasses() {
                return getResourceClasses();
            }
        };

        SeBootstrap.Configuration config = SeBootstrap.Configuration.builder()
                .host("localhost")
                .port(0)
                .build();

        CompletionStage<SeBootstrap.Instance> stage = SeBootstrap.start(app, config);
        server = stage.toCompletableFuture().get();

        int port = server.configuration().port();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @AfterAll
    void stopServer() throws Exception {
        if (server != null) {
            server.stop().toCompletableFuture().get();
        }
        RestAssured.reset();
    }
}