package com.zacharyab24.playground.networksecurity.hmac;

import com.zacharyab24.playground.networksecurity.hmac.model.HMACRequest;
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

@Path("/crypto/hmac")
public class HMACResource {

    private static final Logger logger = Logger.getLogger(HMACResource.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Compute HMAC-SHA256",
            description = "Computes an HMAC-SHA256 message authentication code for the given key and message.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "HMAC hex string.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)),
                    @ApiResponse(responseCode = "400", description = "Invalid request."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error."),
            },
            tags = CRYPTO_TAG
    )
    public Response compute(
            @RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = HMACRequest.class)))
            HMACRequest request) {
        try {
            logger.log(Level.INFO, "Computing HMAC");
            String hmac = HMACService.compute(request);
            return Response.ok(Map.of("hmac", hmac)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "HMAC error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}