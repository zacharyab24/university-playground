package com.zacharyab24.playground.os.concurrency.model;

import com.zacharyab24.comp2240.concurrency.common.TaskResult;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Result of a single scheduled task.")
public record TaskResponse(
        @Schema(description = "Task file this result belongs to.", example = "tasks1.txt")
        String taskFile,
        @Schema(description = "Thread index within the scheduler.", example = "2")
        int threadNo,
        @Schema(description = "Input value to the task.", example = "5")
        int input,
        @Schema(description = "Computed result (input squared).", example = "25")
        int result,
        @Schema(description = "Simulated completion duration in ms.", example = "50")
        long completedAtMillis
) {
    public static TaskResponse fromTaskResult(String taskFile, TaskResult tr) {
        return new TaskResponse(taskFile, tr.threadNo(), tr.input(), tr.result(), tr.completedAtMillis());
    }
}