package com.zacharyab24.playground.os.concurrency.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "A single task group for the concurrency simulation.")
public record ConcurrencyTask(
        @Schema(description = "Name identifying this task group.", example = "tasks1")
        String name,
        @Schema(description = "Number of processors (concurrent threads) to use.", example = "2")
        int numProcesses,
        @Schema(description = "Input values for each task thread.", example = "[5, 3, 8]")
        List<Integer> inputs
) {}