package com.zacharyab24.playground.os.concurrency.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Request containing tasks for a concurrency simulation (semaphore or monitor).")
public record ConcurrencyRequest(
        @Schema(description = "List of task groups to run concurrently.")
        List<ConcurrencyTask> tasks
) {}
