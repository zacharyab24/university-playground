package com.zacharyab24.playground.algorithms.maze;

import com.zacharyab24.comp2230.maze.algorithm.BreadthFirstSearch;
import com.zacharyab24.comp2230.maze.algorithm.DepthFirstSearch;
import com.zacharyab24.comp2230.maze.algorithm.DFSGenerator;
import com.zacharyab24.comp2230.maze.algorithm.SearchResult;
import com.zacharyab24.comp2230.maze.io.MazeReader;
import com.zacharyab24.comp2230.maze.io.MazeWriter;
import com.zacharyab24.comp2230.maze.model.Maze;
import com.zacharyab24.comp2230.maze.model.MazeDto;
import com.zacharyab24.comp2230.maze.model.SolutionPath;
import com.zacharyab24.comp2230.maze.verify.MazeVerification;
import com.zacharyab24.comp2230.maze.verify.VerificationResult;
import com.zacharyab24.playground.algorithms.maze.model.MazeGenerateRequest;
import com.zacharyab24.playground.algorithms.maze.model.MazeSolveResponse;
import com.zacharyab24.playground.algorithms.maze.model.MazeVerifyRequest;

import java.util.List;

public class MazeService {

    public static MazeDto generate(MazeGenerateRequest request) {
        if (request.rows() < 2 || request.cols() < 2) {
            throw new IllegalArgumentException("Maze must be at least 2x2");
        }
        Maze maze = DFSGenerator.generate(request.rows(), request.cols());
        return MazeWriter.toDto(maze);
    }

    public static MazeSolveResponse solve(MazeDto mazeDto) {
        if (mazeDto == null) {
            throw new IllegalArgumentException("Maze is required");
        }

        Maze bfsMaze = MazeReader.fromDto(mazeDto);
        SearchResult bfs = new BreadthFirstSearch().solve(bfsMaze);

        Maze dfsMaze = MazeReader.fromDto(mazeDto);
        SearchResult dfs = new DepthFirstSearch().solve(dfsMaze);

        return new MazeSolveResponse(bfs, dfs);
    }

    public static VerificationResult verify(MazeVerifyRequest request) {
        if (request.maze() == null) {
            throw new IllegalArgumentException("Maze is required");
        }
        Maze maze = MazeReader.fromDto(request.maze());
        List<SolutionPath> solutions = request.solutions() != null ? request.solutions() : List.of();
        return MazeVerification.verify(maze, solutions);
    }
}