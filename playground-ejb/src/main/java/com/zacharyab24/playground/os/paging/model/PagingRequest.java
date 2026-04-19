package com.zacharyab24.playground.os.paging.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Input parameters for the paging simulation.")
public record PagingRequest(
        @Schema(description = "List of processes with their page reference strings.")
        List<SimProcessRequest> processes,
        @Schema(description = "Total number of memory frames available.", example = "30")
        int totalFrames,
        @Schema(description = "Time quantum for the round-robin scheduler.", example = "3")
        int quantum
) {}
