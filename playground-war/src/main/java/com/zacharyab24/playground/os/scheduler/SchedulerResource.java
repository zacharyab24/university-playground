package com.zacharyab24.playground.os.scheduler;

import com.zacharyab24.playground.os.scheduler.model.SchedulerRequest;
import com.zacharyab24.playground.os.scheduler.model.SchedulerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.zacharyab24.playground.api.OpenAPIResource.OS_TAG;

@Path("/scheduler")
public class SchedulerResource {

    private static final Logger logger = Logger.getLogger(SchedulerResource.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Run all scheduling algorithms",
            description = """
                          Runs First-Come-First-Served, Shortest-Process-Next, Preemptive Priority, and Priority Round Robin
                          against the supplied process list and returns the average turnaround and waiting time for each.""",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "One result entry per algorithm.",
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON,
                                array = @ArraySchema(schema = @Schema(implementation = SchedulerResponse.class)),
                                examples = @ExampleObject(
                                        name = "Five-process run",
                                        value = """
                                                [
                                                  {"processorID": "FCFS", "turnAround": "17.20", "waitingTime": "13.00"},
                                                  {"processorID": "SPN",  "turnAround": "10.80", "waitingTime": "6.60"},
                                                  {"processorID": "PP",   "turnAround": "17.80", "waitingTime": "13.60"},
                                                  {"processorID": "PRR",  "turnAround": "17.80", "waitingTime": "13.60"}
                                                ]
                                                """
                                )
                        )
                ),
                @ApiResponse(responseCode = "400", description = "Request body was malformed or failed validation."),
                @ApiResponse(responseCode = "500", description = "Unexpected error while running the scheduler."),
            },
            tags = OS_TAG
    )
    public Response runScheduler(
            @RequestBody(
                    description = "Dispatcher overhead and process list to schedule.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SchedulerRequest.class),
                            examples = @ExampleObject(
                                    name = "Five-process example",
                                    value = """
                                            {
                                              "dispatcher": 1,
                                              "processes": [
                                                {"processorID": "p1", "arrivalTime": 0, "serviceTime": 10, "priority": 0},
                                                {"processorID": "p2", "arrivalTime": 0, "serviceTime": 1,  "priority": 2},
                                                {"processorID": "p3", "arrivalTime": 2, "serviceTime": 4,  "priority": 0},
                                                {"processorID": "p4", "arrivalTime": 0, "serviceTime": 1,  "priority": 1},
                                                {"processorID": "p5", "arrivalTime": 0, "serviceTime": 5,  "priority": 5}
                                              ]
                                            }
                                            """
                            )
                    )
            )
            SchedulerRequest request) {
        try {
            logger.log(Level.INFO, "Running scheduler");
            List<SchedulerResponse> results = SchedulerService.runScheduler(request);
            return Response.ok(results).build();
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
