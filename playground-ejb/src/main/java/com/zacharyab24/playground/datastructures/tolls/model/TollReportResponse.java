package com.zacharyab24.playground.datastructures.tolls.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "Daily toll report after merging and deduplicating both booths.")
public record TollReportResponse(
        @Schema(description = "Total unique vehicles across both booths.", example = "10")
        int totalVehicles,
        @Schema(description = "Total toll income from all unique vehicles.", example = "42.50")
        double totalIncome,
        @Schema(description = "Vehicle count by type.", example = "{\"Car\": 5, \"Truck\": 2, \"Motorcycle\": 2, \"Light Truck\": 1}")
        Map<String, Integer> countByType,
        @Schema(description = "Deduplicated merged toll records from both booths.")
        List<TollRecord> mergedRecords
) {}