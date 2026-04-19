package com.zacharyab24.playground.algorithms.tsp.model;

import com.zacharyab24.comp2230.tsp.model.Coordinate;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Input parameters for the TSP solver.")
public record TSPRequest(
        @Schema(description = "Algorithm to use.", example = "dynamic",
                allowableValues = {"dynamic", "hillclimb"},
                requiredMode = Schema.RequiredMode.REQUIRED)
        String algorithm,
        @Schema(description = "Node coordinates for the TSP instance.")
        List<Coordinate> coordinates,
        @Schema(description = "Iterations for hill climb (ignored for dynamic).", example = "1000000")
        Integer iterations,
        @Schema(description = "Max attempts for hill climb (ignored for dynamic).", example = "100")
        Integer maxAttempts
) {}