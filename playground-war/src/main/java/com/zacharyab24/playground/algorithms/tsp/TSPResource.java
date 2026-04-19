package com.zacharyab24.playground.algorithms.tsp;

import com.zacharyab24.comp2230.tsp.model.TSPResult;
import com.zacharyab24.playground.algorithms.tsp.model.TSPRequest;
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

import static com.zacharyab24.playground.api.OpenAPIResource.ALGORITHMS_TAG;

@Path("/tsp")
public class TSPResource {

    private static final Logger logger = Logger.getLogger(TSPResource.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Solve a Travelling Salesman Problem",
            description = """
                          Solves a TSP instance using either Dynamic Programming (exact) or Hill Climbing (heuristic).
                          Provide node coordinates and the algorithm will find a tour visiting all nodes.""",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Optimal or near-optimal tour found.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = TSPResult.class),
                                    examples = @ExampleObject(
                                            name = "Three-node tour",
                                            value = """
                                                {"path": [0, 1, 2, 0], "length": 20.0}
                                                """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Request body was malformed or failed validation."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error while solving."),
            },
            tags = ALGORITHMS_TAG
    )
    public Response solve(
            @RequestBody(
                    description = "Node coordinates and algorithm choice.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TSPRequest.class),
                            examples = @ExampleObject(
                                    name = "Dynamic programming example",
                                    value = """
                                            {
                                              "algorithm": "dynamic",
                                              "coordinates": [
                                                {"x": 0, "y": 0},
                                                {"x": 3, "y": 4},
                                                {"x": 6, "y": 0}
                                              ]
                                            }
                                            """
                            )
                    )
            )
            TSPRequest request) {
        try {
            logger.log(Level.INFO, "Running TSP solver");
            TSPResult result = TSPService.solve(request);
            return Response.ok(result).build();
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, () -> "Unable to parse request: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Unexpected error whilst solving TSP: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }
}