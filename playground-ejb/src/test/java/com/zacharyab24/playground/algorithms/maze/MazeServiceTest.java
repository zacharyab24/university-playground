package com.zacharyab24.playground.algorithms.maze;

import com.zacharyab24.comp2230.maze.model.MazeDto;
import com.zacharyab24.comp2230.maze.model.SolutionPath;
import com.zacharyab24.comp2230.maze.verify.VerificationResult;
import com.zacharyab24.playground.algorithms.maze.model.MazeGenerateRequest;
import com.zacharyab24.playground.algorithms.maze.model.MazeSolveResponse;
import com.zacharyab24.playground.algorithms.maze.model.MazeVerifyRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MazeServiceTest {

    @Test
    void generate_returnsValidMazeDto() {
        MazeGenerateRequest request = new MazeGenerateRequest(5, 5);

        MazeDto maze = MazeService.generate(request);

        assertThat(maze.rows()).isEqualTo(5);
        assertThat(maze.cols()).isEqualTo(5);
        assertThat(maze.connectivity()).hasSize(25);
        assertThat(maze.start()).isGreaterThan(0);
        assertThat(maze.finish()).isGreaterThan(0);
        assertThat(maze.start()).isNotEqualTo(maze.finish());
    }

    @Test
    void generate_tooSmall_throwsIllegalArgument() {
        MazeGenerateRequest request = new MazeGenerateRequest(1, 5);

        assertThatThrownBy(() -> MazeService.generate(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("2x2");
    }

    @Test
    void solve_returnsBfsAndDfs() {
        MazeDto mazeDto = MazeService.generate(new MazeGenerateRequest(5, 5));

        MazeSolveResponse response = MazeService.solve(mazeDto);

        assertThat(response.bfs()).isNotNull();
        assertThat(response.dfs()).isNotNull();
        assertThat(response.bfs().solution()).isNotEmpty();
        assertThat(response.dfs().solution()).isNotEmpty();
        assertThat(response.bfs().stepCount()).isGreaterThan(0);
        assertThat(response.dfs().stepCount()).isGreaterThan(0);
    }

    @Test
    void solve_solutionsStartAndEndCorrectly() {
        MazeDto mazeDto = MazeService.generate(new MazeGenerateRequest(3, 3));

        MazeSolveResponse response = MazeService.solve(mazeDto);

        assertThat(response.bfs().solution().getFirst()).isEqualTo(mazeDto.start());
        assertThat(response.bfs().solution().getLast()).isEqualTo(mazeDto.finish());
        assertThat(response.dfs().solution().getFirst()).isEqualTo(mazeDto.start());
        assertThat(response.dfs().solution().getLast()).isEqualTo(mazeDto.finish());
    }

    @Test
    void verify_generatedMaze_isValid() {
        MazeDto mazeDto = MazeService.generate(new MazeGenerateRequest(5, 5));
        MazeVerifyRequest request = new MazeVerifyRequest(mazeDto, List.of());

        VerificationResult result = MazeService.verify(request);

        assertThat(result.cellsWithAllWalls()).isZero();
        assertThat(result.cellsWithNoWalls()).isZero();
        assertThat(result.hasCycles()).isFalse();
        assertThat(result.allReachable()).isTrue();
    }

    @Test
    void verify_withSolution_validatesSolution() {
        MazeDto mazeDto = MazeService.generate(new MazeGenerateRequest(3, 3));
        MazeSolveResponse solved = MazeService.solve(mazeDto);

        SolutionPath path = new SolutionPath(
                solved.bfs().stepCount(),
                solved.bfs().solution()
        );
        MazeVerifyRequest request = new MazeVerifyRequest(mazeDto, List.of(path));

        VerificationResult result = MazeService.verify(request);

        assertThat(result.solutionResults()).hasSize(1);
    }

    @Test
    void solve_nullMaze_throwsIllegalArgument() {
        assertThatThrownBy(() -> MazeService.solve(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}