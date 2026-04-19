package com.zacharyab24.playground.api;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("/")
@OpenAPIDefinition(
        info = @Info(
                title = "University Playground API",
                version = "1.0.0",
                description = """
                        REST API exposing refactored university assignments as services.
                        Covers operating systems (scheduling, concurrency, paging), graph algorithms (TSP, maze),
                        data structures (linked list, BSTree, hash table), and network security (RSA, DH, AES-CBC, HMAC)."""
        ),
        servers = {
                @Server(url = "localhost:8080/playground/api", description = "Wildfly (local)"),
                @Server(url = "api.playground.zacbower.com", description = "Prod (future)")
        },
        tags = {
                @Tag(name = "Operating Systems", description = "COMP2240 — scheduling, concurrency, and paging simulations."),
                @Tag(name = "Algorithms", description = "COMP2230 — TSP solver and maze generation/solving/verification."),
                @Tag(name = "Data Structures", description = "SENG1120 — linked list toll reports and BSTree/HTable inventory stats."),
                @Tag(name = "Network Security", description = "SENG2250 — RSA, Diffie-Hellman, AES-CBC, and HMAC cryptographic primitives."),
        }
)
public class OpenAPIResource {

    public static final String OS_TAG = "Operating Systems";
    public static final String ALGORITHMS_TAG = "Algorithms";
    public static final String DATA_STRUCTURES_TAG = "Data Structures";
    public static final String CRYPTO_TAG = "Network Security";

    private static final Set<String> RESOURCE_PACKAGES = Set.of(
            "com.zacharyab24.playground.os",
            "com.zacharyab24.playground.algorithms",
            "com.zacharyab24.playground.datastructures",
            "com.zacharyab24.playground.networksecurity",
            "com.zacharyab24.playground.api"
    );

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOpenApiSpec() {
        try {
            SwaggerConfiguration config = new SwaggerConfiguration()
                    .resourcePackages(RESOURCE_PACKAGES)
                    .prettyPrint(true);

            OpenAPI openAPI = new JaxrsOpenApiContextBuilder<>()
                    .openApiConfiguration(config)
                    .buildContext(true)
                    .read();

            return Response.ok(Json.pretty(openAPI)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }
}