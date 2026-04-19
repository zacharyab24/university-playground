package com.zacharyab24.playground.algorithms.maze.model;

import com.zacharyab24.comp2230.maze.algorithm.SearchResult;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Solutions for a maze using BFS and DFS.")
public record MazeSolveResponse(
        @Schema(description = "Breadth-first search result.")
        SearchResult bfs,
        @Schema(description = "Depth-first search result.")
        SearchResult dfs
) {}