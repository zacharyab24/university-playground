package com.zacharyab24.playground.os.scheduler.model;

import com.zacharyab24.comp2240.scheduling.model.Process;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A single process to be scheduled.")
public record ProcessDto(
        @Schema(description = "Unique identifier for the process.", example = "p1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String processorID,

        @Schema(description = "Tick at which the process enters the ready queue.", example = "0",
                requiredMode = Schema.RequiredMode.REQUIRED)
        int arrivalTime,

        @Schema(description = "Total CPU time required to complete the process.", example = "10",
                requiredMode = Schema.RequiredMode.REQUIRED)
        int serviceTime,

        @Schema(description = "Scheduling priority; lower numbers run first (used by PP and PRR).",
                example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        int priority
) {
    public Process toProcess() {
        return new Process(processorID, arrivalTime, serviceTime, priority);
    }
}