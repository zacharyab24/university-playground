package com.zacharyab24.playground.algorithms.maze.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Input parameters for maze generation.")
public record MazeGenerateRequest(
        @Schema(description = "Number of rows.", example = "10",
                requiredMode = Schema.RequiredMode.REQUIRED)
        int rows,
        @Schema(description = "Number of columns.", example = "10",
                requiredMode = Schema.RequiredMode.REQUIRED)
        int cols
) {}