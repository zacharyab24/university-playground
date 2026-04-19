package com.zacharyab24.playground.algorithms.maze.model;

import com.zacharyab24.comp2230.maze.model.MazeDto;
import com.zacharyab24.comp2230.maze.model.SolutionPath;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Input for maze verification.")
public record MazeVerifyRequest(
        @Schema(description = "The maze to verify.")
        MazeDto maze,
        @Schema(description = "Solution paths to validate against the maze.")
        List<SolutionPath> solutions
) {}