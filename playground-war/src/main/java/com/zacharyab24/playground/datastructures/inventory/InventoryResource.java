package com.zacharyab24.playground.datastructures.inventory;

import com.zacharyab24.playground.datastructures.inventory.model.BenchmarkRequest;
import com.zacharyab24.playground.datastructures.inventory.model.BenchmarkResponse;
import com.zacharyab24.playground.datastructures.inventory.model.InventoryStatsRequest;
import com.zacharyab24.playground.datastructures.inventory.model.InventoryStatsResponse;
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

import static com.zacharyab24.playground.api.OpenAPIResource.DATA_STRUCTURES_TAG;

@Path("/inventory")
public class InventoryResource {

    private static final Logger logger = Logger.getLogger(InventoryResource.class.getName());

    @POST
    @Path("/stats")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Calculate inventory statistics",
            description = """
                          Loads parts into the chosen data structure (BSTree or HTable) and calculates
                          distinct part count, total inventory, and parts below a threshold.""",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Inventory statistics.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = InventoryStatsResponse.class),
                                    examples = @ExampleObject(
                                            name = "BSTree stats",
                                            value = """
                                                {
                                                  "dataStructure": "BSTREE",
                                                  "totalParts": 3,
                                                  "totalInventory": 155,
                                                  "partsLessThan": 1,
                                                  "contents": "(AAA, 50) (BBB, 5) (CCC, 100)"
                                                }
                                                """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Request body was malformed or failed validation."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error."),
            },
            tags = DATA_STRUCTURES_TAG
    )
    public Response getStats(
            @RequestBody(
                    description = "Data structure type, parts, and threshold.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = InventoryStatsRequest.class),
                            examples = @ExampleObject(
                                    name = "BSTree example",
                                    value = """
                                            {
                                              "dataStructure": "BSTREE",
                                              "parts": [
                                                {"code": "AAA", "quantity": 50},
                                                {"code": "BBB", "quantity": 5},
                                                {"code": "CCC", "quantity": 100}
                                              ],
                                              "lessThanThreshold": 10
                                            }
                                            """
                            )
                    )
            )
            InventoryStatsRequest request) {
        try {
            logger.log(Level.INFO, "Calculating inventory stats");
            InventoryStatsResponse response = InventoryService.getStats(request);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, () -> "Unable to parse request: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Unexpected error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/benchmark")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Benchmark data structure performance",
            description = """
                          Runs repeated add/remove cycles on the chosen data structure and measures
                          total elapsed time and average time per operation.""",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Benchmark timing results.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = BenchmarkResponse.class),
                                    examples = @ExampleObject(
                                            name = "BSTree benchmark",
                                            value = """
                                                {
                                                  "dataStructure": "BSTREE",
                                                  "iterations": 2200000,
                                                  "totalTimeMs": 500,
                                                  "timePerOperationMs": 0.00023
                                                }
                                                """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Request body was malformed or failed validation."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error."),
            },
            tags = DATA_STRUCTURES_TAG
    )
    public Response benchmark(
            @RequestBody(
                    description = "Data structure type, parts, and iteration count.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = BenchmarkRequest.class),
                            examples = @ExampleObject(
                                    name = "BSTree benchmark",
                                    value = """
                                            {
                                              "dataStructure": "BSTREE",
                                              "parts": [
                                                {"code": "AAA", "quantity": 50},
                                                {"code": "BBB", "quantity": 5}
                                              ],
                                              "iterations": 100000
                                            }
                                            """
                            )
                    )
            )
            BenchmarkRequest request) {
        try {
            logger.log(Level.INFO, "Running inventory benchmark");
            BenchmarkResponse response = InventoryService.benchmark(request);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, () -> "Unable to parse request: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Unexpected error: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }
}