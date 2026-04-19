package com.zacharyab24.playground.os.concurrency;

import com.zacharyab24.comp2240.concurrency.monitor.MonitorScheduler;
import com.zacharyab24.comp2240.concurrency.semaphore.SemaphoreScheduler;
import com.zacharyab24.playground.os.concurrency.model.ConcurrencyRequest;
import com.zacharyab24.playground.os.concurrency.model.TaskResponse;
import com.zacharyab24.playground.os.concurrency.model.WormholeRequest;
import com.zacharyab24.playground.os.concurrency.model.WormholeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.zacharyab24.playground.api.OpenAPIResource.OS_TAG;

@Path("/multithreading")
public class ConcurrencyResource {
    private static final Logger logger = Logger.getLogger(ConcurrencyResource.class.getName());

    @POST
    @Path("/wormhole")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Run wormhole traveller simulation",
            description = """
                          Simulates travellers crossing a wormhole between Earth and Proxima-b.
                          Uses a semaphore to ensure only one traveller can use the wormhole at a time.""",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Simulation completed with crossing results.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = WormholeResponse.class),
                                    examples = @ExampleObject(
                                            name = "Two travellers, one trip each",
                                            value = """
                                                {
                                                  "totalCrossings": 2,
                                                  "crossings": [
                                                    {"travellerId": "E_H1", "from": "EARTH", "to": "PROXIMA_B", "crossingNumber": 1},
                                                    {"travellerId": "P_A1", "from": "PROXIMA_B", "to": "EARTH", "crossingNumber": 1}
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
    public Response runWormholeSimulation(WormholeRequest request) {
        try {
            logger.log(Level.INFO, "Running wormhole simulation");
            WormholeResponse response = WormholeService.runSimulation(request);
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

    @POST
    @Path("/semaphore")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Run semaphore concurrency simulation",
            description = """
                          Runs tasks using semaphore-based concurrency control.
                          A semaphore limits the number of tasks executing concurrently to the specified number of processors.""",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task results from the semaphore simulation.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)),
                                    examples = @ExampleObject(
                                            name = "Two tasks",
                                            value = """
                                                [
                                                  {"taskFile": "tasks1", "threadNo": 1, "input": 5, "result": 25, "completedAtMillis": 50},
                                                  {"taskFile": "tasks1", "threadNo": 2, "input": 3, "result": 9, "completedAtMillis": 100}
                                                ]
                                                """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Request body was malformed or failed validation."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error while running the simulation."),
            },
            tags = OS_TAG
    )
    public Response runSemaphoreSimulation(ConcurrencyRequest request) {
        try {
        logger.log(Level.INFO, "Running semaphore simulation");
            List<TaskResponse> responses = ConcurrencyService.runSimulation(request, SemaphoreScheduler::new);
            return Response.ok(responses).build();
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, () -> "Unable to parse request: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Unexpected error whilst running program: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/monitor")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Run monitor concurrency simulation",
            description = """
                          Runs tasks using monitor-based (synchronized) concurrency control.
                          Tasks are executed one at a time using Java's synchronized mechanism.""",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task results from the monitor simulation.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)),
                                    examples = @ExampleObject(
                                            name = "Two tasks",
                                            value = """
                                                [
                                                  {"taskFile": "tasks1", "threadNo": 1, "input": 5, "result": 25, "completedAtMillis": 50},
                                                  {"taskFile": "tasks1", "threadNo": 2, "input": 3, "result": 9, "completedAtMillis": 100}
                                                ]
                                                """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Request body was malformed or failed validation."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error while running the simulation."),
            },
            tags = OS_TAG
    )
    public Response runMonitorSimulation(ConcurrencyRequest request) {
        try {
            logger.log(Level.INFO, "Running monitor simulation");
            List<TaskResponse> responses = ConcurrencyService.runSimulation(request, MonitorScheduler::new);
            return Response.ok(responses).build();
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, () -> "Unable to parse request: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Unexpected error whilst running program: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
