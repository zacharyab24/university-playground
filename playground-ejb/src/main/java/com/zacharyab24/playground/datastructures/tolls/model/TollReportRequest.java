package com.zacharyab24.playground.datastructures.tolls.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Toll records from two booths to merge into a daily report.")
public record TollReportRequest(
        @Schema(description = "Toll records from booth 1.")
        List<TollRecord> booth1,
        @Schema(description = "Toll records from booth 2.")
        List<TollRecord> booth2
) {}