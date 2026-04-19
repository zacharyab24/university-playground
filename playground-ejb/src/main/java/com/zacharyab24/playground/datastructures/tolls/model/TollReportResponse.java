package com.zacharyab24.playground.datastructures.tolls.model;

import java.util.List;
import java.util.Map;

// TODO: Add @Schema annotations
public record TollReportResponse(
        int totalVehicles,
        double totalIncome,
        Map<String, Integer> countByType,
        List<TollRecord> mergedRecords
) {}