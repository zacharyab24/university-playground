package com.zacharyab24.playground.datastructures.inventory.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Input parameters for the data structure benchmark.")
public record BenchmarkRequest(
        @Schema(description = "Data structure to benchmark.", example = "BSTREE",
                requiredMode = Schema.RequiredMode.REQUIRED)
        DataStructures dataStructure,
        @Schema(description = "Parts to use in the benchmark.")
        List<PartRecord> parts,
        @Schema(description = "Number of add/remove cycles to run (max 1,000,000).", example = "100000")
        int iterations
) {}