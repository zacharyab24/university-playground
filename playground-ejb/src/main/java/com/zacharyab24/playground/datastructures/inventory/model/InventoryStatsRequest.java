package com.zacharyab24.playground.datastructures.inventory.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Input parameters for inventory statistics.")
public record InventoryStatsRequest(
        @Schema(description = "Data structure to use.", example = "BSTREE",
                requiredMode = Schema.RequiredMode.REQUIRED)
        DataStructures dataStructure,
        @Schema(description = "Parts to load into the data structure.")
        List<PartRecord> parts,
        @Schema(description = "Threshold for counting parts with quantity below this value.", example = "10")
        int lessThanThreshold
) {}