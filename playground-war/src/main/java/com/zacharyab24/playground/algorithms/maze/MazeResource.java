package com.zacharyab24.playground.algorithms.maze;

import com.zacharyab24.comp2230.maze.model.MazeDto;
import com.zacharyab24.comp2230.maze.verify.VerificationResult;
import com.zacharyab24.playground.algorithms.maze.model.MazeGenerateRequest;
import com.zacharyab24.playground.algorithms.maze.model.MazeSolveResponse;
import com.zacharyab24.playground.algorithms.maze.model.MazeVerifyRequest;
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

@Path("/maze")
public class MazeResource {

    private static final Logger logger = Logger.getLogger(MazeResource.class.getName());

    @POST
    @Path("/generate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Generate a random maze",
            description = "Generates a random maze using DFS-based generation. The maze is guaranteed to have a path from start to finish.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Generated maze.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = MazeDto.class),
                                    examples = @ExampleObject(
                                            name = "2x2 maze",
                                            value = """
                                                {"rows": 2, "cols": 2, "start": 1, "finish": 4, "connectivity": [3, 2, 1, 0]}
                                                """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Request body was malformed or failed validation."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error during generation."),
            },
            tags = ALGORITHMS_TAG
    )
    public Response generate(
            @RequestBody(
                    description = "Maze dimensions.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MazeGenerateRequest.class),
                            examples = @ExampleObject(
                                    name = "10x10 maze",
                                    value = """
                                            {"rows": 10, "cols": 10}
                                            """
                            )
                    )
            )
            MazeGenerateRequest request) {
        try {
            logger.log(Level.INFO, "Generating maze");
            MazeDto maze = MazeService.generate(request);
            return Response.ok(maze).build();
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, () -> "Unable to parse request: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Unexpected error generating maze: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/solve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Solve a maze",
            description = "Solves the provided maze using both Breadth-First Search and Depth-First Search, returning both solutions.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "BFS and DFS solutions.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = MazeSolveResponse.class),
                                    examples = @ExampleObject(
                                            name = "2x2 maze solved",
                                            value = """
                                                {
                                                  "bfs": {"solution": [1, 2, 4], "stepCount": 3, "totalStepCount": 4},
                                                  "dfs": {"solution": [1, 2, 4], "stepCount": 3, "totalStepCount": 4}
                                                }
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
                    description = "Maze to solve.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MazeDto.class),
                            examples = @ExampleObject(
                                    name = "2x2 maze",
                                    value = """
                                            {"rows": 2, "cols": 2, "start": 1, "finish": 4, "connectivity": [3, 2, 1, 0]}
                                            """
                            )
                    )
            )
            MazeDto mazeDto) {
        try {
            logger.log(Level.INFO, "Solving maze");
            MazeSolveResponse response = MazeService.solve(mazeDto);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, () -> "Unable to parse request: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Unexpected error solving maze: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/verify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Verify a maze and its solutions",
            description = """
                          Verifies structural properties of a maze (no enclosed cells, no open cells, no cycles,
                          all cells reachable) and validates any provided solution paths.""",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Verification results.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = VerificationResult.class),
                                    examples = @ExampleObject(
                                            name = "Valid maze",
                                            value = """
                                                {
                                                  "cellsWithAllWalls": 0,
                                                  "cellsWithNoWalls": 0,
                                                  "hasCycles": false,
                                                  "allReachable": true,
                                                  "solutionResults": ["valid"]
                                                }
                                                """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Request body was malformed or failed validation."),
                    @ApiResponse(responseCode = "500", description = "Unexpected error during verification."),
            },
            tags = ALGORITHMS_TAG
    )
    public Response verify(
            @RequestBody(
                    description = "Maze and solution paths to verify.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MazeVerifyRequest.class)
                    )
            )
            MazeVerifyRequest request) {
        try {
            logger.log(Level.INFO, "Verifying maze");
            VerificationResult result = MazeService.verify(request);
            return Response.ok(result).build();
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, () -> "Unable to parse request: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, () -> "Unexpected error verifying maze: %s".formatted(e.getLocalizedMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }
}