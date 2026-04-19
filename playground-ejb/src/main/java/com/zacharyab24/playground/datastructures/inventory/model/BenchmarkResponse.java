package com.zacharyab24.playground.datastructures.inventory.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Results of a data structure add/remove benchmark.")
public record BenchmarkResponse(
        @Schema(description = "Data structure benchmarked.", example = "BSTREE")
        DataStructures dataStructure,
        @Schema(description = "Total number of operations (removes + adds).", example = "2200000")
        int iterations,
        @Schema(description = "Total elapsed time in milliseconds.", example = "500")
        long totalTimeMs,
        @Schema(description = "Average time per operation in milliseconds.", example = "0.00023")
        double timePerOperationMs
) {}