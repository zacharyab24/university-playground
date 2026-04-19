package com.zacharyab24.playground.os.scheduler.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Input payload for the scheduling simulation.")
public record SchedulerRequest(
        @Schema(description = "Dispatcher overhead (ticks charged on every context switch).",
                example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        int dispatcher,

        @Schema(description = "Processes to feed through each scheduling algorithm.",
                requiredMode = Schema.RequiredMode.REQUIRED)
        List<ProcessDto> processes
) {}