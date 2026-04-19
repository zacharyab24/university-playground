package com.zacharyab24.playground.datastructures.inventory.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Inventory statistics computed from the chosen data structure.")
public record InventoryStatsResponse(
        @Schema(description = "Data structure used.", example = "BSTREE")
        DataStructures dataStructure,
        @Schema(description = "Number of distinct part types.", example = "3")
        int totalParts,
        @Schema(description = "Sum of all part quantities.", example = "155")
        int totalInventory,
        @Schema(description = "Number of parts with quantity below the threshold.", example = "1")
        int partsLessThan,
        @Schema(description = "Data structure contents as a string (ordering depends on structure type).",
                example = "(AAA, 50) (BBB, 5) (CCC, 100)")
        String contents
) {}