package com.zacharyab24.playground.os.paging;

import com.zacharyab24.playground.os.paging.model.PagingRequest;
import com.zacharyab24.playground.os.paging.model.PagingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

import static com.zacharyab24.playground.api.OpenAPIResource.OS_TAG;

@Path("/paging")
public class PagingResource {
    private static final Logger logger = Logger.getLogger(PagingResource.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Run paging simulation",
            description = """
                          Runs a round-robin paging simulation using both Fixed-Local (Clock) and Variable-Global (Clock)
                          replacement policies against the supplied process list and returns the results for each.""",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Simulation results for each replacement policy.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = PagingResponse.class),
                                    examples = @ExampleObject(
                                            name = "Two-process run",
                                            value = """
                                                {
                                                  "results": [
                                                    {
                                                      "algorithm": "Fixed-Local",
                                                      "processes": [
                                                        {"pid": 1, "processName": "Process1", "finishTime": 25, "faults": 3, "faultTimes": "{0, 7, 16}"},
                                                        {"pid": 2, "processName": "Process2", "finishTime": 30, "faults": 4, "faultTimes": "{0, 8, 14, 22}"}
                                                      ]
                                                    },
                                                    {
                                                      "algorithm": "Variable-Global",
                                                      "processes": [
                                                        {"pid": 1, "processName": "Process1", "finishTime": 24, "faults": 2, "faultTimes": "{0, 12}"},
                                                        {"pid": 2, "processName": "Process2", "finishTime": 28, "faults": 3, "faultTimes": "{0, 8, 18}"}
                                                      ]
                                                    }
                                                  ]
                                                }
                                                """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Request body was malformed or failed validation."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error while running the simulation."),
            },
            tags = OS_TAG
    )
    public Response runPagingSimulation(
            @RequestBody(
                    description = "Process list, frame count, and time quantum for the simulation.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PagingRequest.class),
                            examples = @ExampleObject(
                                    name = "Two-process example",
                                    value = """
                                            {
                                              "totalFrames": 30,
                                              "quantum": 3,
                                              "processes": [
                                                {"pid": 1, "name": "Process1", "pages": [1, 2, 3, 1, 4]},
                                                {"pid": 2, "name": "Process2", "pages": [2, 3, 4, 2, 1]}
                                              ]
                                            }
                                            """
                            )
                    )
            )
            PagingRequest request) {
        try {
            logger.log(Level.INFO, "Running paging simulation");
            PagingResponse response = PagingService.runPagingSimulation(request);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, () -> "Unable to parse request: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Unexpected error whilst running simulation: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
