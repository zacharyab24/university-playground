package com.zacharyab24.playground.networksecurity.dh;

import com.zacharyab24.playground.networksecurity.dh.model.DHKeyPairResponse;
import com.zacharyab24.playground.networksecurity.dh.model.DHSessionRequest;
import com.zacharyab24.playground.networksecurity.dh.model.DHSessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.zacharyab24.playground.api.OpenAPIResource.CRYPTO_TAG;

@Path("/crypto/dh")
public class DHResource {

    private static final Logger logger = Logger.getLogger(DHResource.class.getName());

    @POST
    @Path("/generate")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Generate a Diffie-Hellman key pair",
            description = "Generates a random DH private key and computes the corresponding public key using hardcoded 1024-bit prime parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Generated DH key pair.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = DHKeyPairResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected error."),
            },
            tags = CRYPTO_TAG
    )
    public Response generate() {
        try {
            logger.log(Level.INFO, "Generating DH key pair");
            return Response.ok(DHService.generateKeyPair()).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "DH generate error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/session")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Derive a shared session key",
            description = "Computes a shared session key from the other party's DH public key and your DH private key, then hashes it with SHA-256.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shared session key and its hash.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = DHSessionResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error."),
            },
            tags = CRYPTO_TAG
    )
    public Response session(
            @RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = DHSessionRequest.class)))
            DHSessionRequest request) {
        try {
            logger.log(Level.INFO, "Deriving DH session key");
            return Response.ok(DHService.deriveSessionKey(request)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "DH session error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}