package com.zacharyab24.playground.datastructures.tolls.model;

import java.util.List;

// TODO: Add @Schema annotations
public record TollReportRequest(
        List<TollRecord> booth1,
        List<TollRecord> booth2
) {}