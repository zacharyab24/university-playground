package com.zacharyab24.playground.networksecurity.rsa;

import com.zacharyab24.playground.networksecurity.rsa.model.*;
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

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.zacharyab24.playground.api.OpenAPIResource.CRYPTO_TAG;

@Path("/crypto/rsa")
public class RSAResource {

    private static final Logger logger = Logger.getLogger(RSAResource.class.getName());

    @POST
    @Path("/generate")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Generate an RSA key pair",
            description = "Generates a new 1024-bit RSA key pair with public exponent e = 65537.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Generated key pair.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = RSAKeyPairResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected error."),
            },
            tags = CRYPTO_TAG
    )
    public Response generate() {
        try {
            logger.log(Level.INFO, "Generating RSA key pair");
            return Response.ok(RSAService.generateKeyPair()).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Error generating RSA keys: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/encrypt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Encrypt with RSA",
            description = "Encrypts a plaintext message using an RSA public key.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ciphertext.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)),
                    @ApiResponse(responseCode = "400", description = "Invalid request."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error."),
            },
            tags = CRYPTO_TAG
    )
    public Response encrypt(
            @RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = RSAEncryptRequest.class)))
            RSAEncryptRequest request) {
        try {
            logger.log(Level.INFO, "RSA encrypt");
            String ciphertext = RSAService.encrypt(request);
            return Response.ok(Map.of("ciphertext", ciphertext)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "RSA encrypt error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/decrypt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Decrypt with RSA",
            description = "Decrypts a ciphertext using an RSA private key.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Decrypted plaintext.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)),
                    @ApiResponse(responseCode = "400", description = "Invalid request."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error."),
            },
            tags = CRYPTO_TAG
    )
    public Response decrypt(
            @RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = RSADecryptRequest.class)))
            RSADecryptRequest request) {
        try {
            logger.log(Level.INFO, "RSA decrypt");
            String plaintext = RSAService.decrypt(request);
            return Response.ok(Map.of("plaintext", plaintext)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "RSA decrypt error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/sign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Sign a message with RSA",
            description = "Generates an RSA signature for a message using a private key.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Signature.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)),
                    @ApiResponse(responseCode = "400", description = "Invalid request."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error."),
            },
            tags = CRYPTO_TAG
    )
    public Response sign(
            @RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = RSASignRequest.class)))
            RSASignRequest request) {
        try {
            logger.log(Level.INFO, "RSA sign");
            String signature = RSAService.sign(request);
            return Response.ok(Map.of("signature", signature)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "RSA sign error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/verify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Verify an RSA signature",
            description = "Verifies an RSA signature against a message and public key.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Verification result.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)),
                    @ApiResponse(responseCode = "400", description = "Invalid request."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error."),
            },
            tags = CRYPTO_TAG
    )
    public Response verify(
            @RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = RSAVerifyRequest.class)))
            RSAVerifyRequest request) {
        try {
            logger.log(Level.INFO, "RSA verify");
            boolean valid = RSAService.verify(request);
            return Response.ok(Map.of("valid", valid)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "RSA verify error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}