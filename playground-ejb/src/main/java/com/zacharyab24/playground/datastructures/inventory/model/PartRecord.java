package com.zacharyab24.playground.datastructures.inventory.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A mechanical part in the inventory.")
public record PartRecord(
        @Schema(description = "Unique part code.", example = "AAA",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String code,
        @Schema(description = "Quantity in inventory.", example = "50",
                requiredMode = Schema.RequiredMode.REQUIRED)
        int quantity
) {}