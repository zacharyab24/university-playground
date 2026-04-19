package com.zacharyab24.playground.networksecurity.cbc;

import com.zacharyab24.playground.networksecurity.cbc.model.*;
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

@Path("/crypto/cbc")
public class CBCResource {

    private static final Logger logger = Logger.getLogger(CBCResource.class.getName());

    @POST
    @Path("/generate-key")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Generate an AES key and IV",
            description = "Generates a random AES key of the specified size and a random 16-byte initialization vector.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Generated key and IV.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = CBCKeyResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid key size."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error."),
            },
            tags = CRYPTO_TAG
    )
    public Response generateKey(
            @RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = CBCKeyRequest.class)))
            CBCKeyRequest request) {
        try {
            logger.log(Level.INFO, "Generating AES key");
            return Response.ok(CBCService.generateKey(request)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "CBC key gen error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/encrypt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Encrypt with AES-CBC",
            description = "Encrypts a plaintext message using AES in Cipher Block Chaining mode.",
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
                    content = @Content(schema = @Schema(implementation = CBCEncryptRequest.class)))
            CBCEncryptRequest request) {
        try {
            logger.log(Level.INFO, "CBC encrypt");
            String ciphertext = CBCService.encrypt(request);
            return Response.ok(Map.of("ciphertext", ciphertext)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "CBC encrypt error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/decrypt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Decrypt with AES-CBC",
            description = "Decrypts a ciphertext using AES in Cipher Block Chaining mode.",
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
                    content = @Content(schema = @Schema(implementation = CBCDecryptRequest.class)))
            CBCDecryptRequest request) {
        try {
            logger.log(Level.INFO, "CBC decrypt");
            String plaintext = CBCService.decrypt(request);
            return Response.ok(Map.of("plaintext", plaintext)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "CBC decrypt error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}