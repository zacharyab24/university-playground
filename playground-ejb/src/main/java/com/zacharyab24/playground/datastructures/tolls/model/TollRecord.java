package com.zacharyab24.playground.datastructures.tolls.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A single electronic toll transaction record.")
public record TollRecord(
        @Schema(description = "Vehicle licence plate.", example = "ABC123",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String license,
        @Schema(description = "Vehicle type.", example = "Car",
                allowableValues = {"Car", "Truck", "Motorcycle", "Light Truck"},
                requiredMode = Schema.RequiredMode.REQUIRED)
        String type,
        @Schema(description = "Toll charge amount.", example = "3.50",
                requiredMode = Schema.RequiredMode.REQUIRED)
        double charge
) {}