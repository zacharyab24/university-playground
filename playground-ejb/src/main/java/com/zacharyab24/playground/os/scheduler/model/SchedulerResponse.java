package com.zacharyab24.playground.os.scheduler.model;

import com.zacharyab24.comp2240.scheduling.model.Result;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Per-algorithm summary returned by the scheduler.")
public record SchedulerResponse(
        @Schema(description = "Name of the scheduling algorithm this result belongs to.",
                example = "FCFS",
                allowableValues = {"FCFS", "SPN", "PP", "PRR"})
        String processorID,

        @Schema(description = "Average turnaround time across the process list (ticks).",
                example = "17.20")
        String turnAround,

        @Schema(description = "Average waiting time across the process list (ticks).",
                example = "13.00")
        String waitingTime
) {
    public static SchedulerResponse fromResult(Result result) {
        return new SchedulerResponse(result.processorID(), result.turnAround(), result.waitingTime());
    }
}