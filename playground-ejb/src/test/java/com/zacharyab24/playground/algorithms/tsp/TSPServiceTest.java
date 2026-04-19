package com.zacharyab24.playground.algorithms.tsp;

import com.zacharyab24.comp2230.tsp.model.Coordinate;
import com.zacharyab24.comp2230.tsp.model.TSPResult;
import com.zacharyab24.playground.algorithms.tsp.model.TSPRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TSPServiceTest {

    private static final List<Coordinate> TRIANGLE = List.of(
            new Coordinate(0, 0),
            new Coordinate(3, 4),
            new Coordinate(6, 0)
    );

    @Test
    void solve_dynamic_returnsValidTour() {
        TSPRequest request = new TSPRequest("dynamic", TRIANGLE, null, null);

        TSPResult result = TSPService.solve(request);

        assertThat(result.path()).hasSize(4); // 3 nodes + return to start
        assertThat(result.path().getFirst()).isEqualTo(result.path().getLast()); // round trip
        assertThat(result.length()).isGreaterThan(0);
    }

    @Test
    void solve_hillclimb_returnsValidTour() {
        TSPRequest request = new TSPRequest("hillclimb", TRIANGLE, 1000, 10);

        TSPResult result = TSPService.solve(request);

        assertThat(result.path()).hasSize(4);
        assertThat(result.path().getFirst()).isEqualTo(result.path().getLast());
        assertThat(result.length()).isGreaterThan(0);
    }

    @Test
    void solve_hillclimb_missingParams_throwsIllegalArgument() {
        TSPRequest request = new TSPRequest("hillclimb", TRIANGLE, null, null);

        assertThatThrownBy(() -> TSPService.solve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("iterations");
    }

    @Test
    void solve_unknownAlgorithm_throwsIllegalArgument() {
        TSPRequest request = new TSPRequest("unknown", TRIANGLE, null, null);

        assertThatThrownBy(() -> TSPService.solve(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void solve_tooFewCoordinates_throwsIllegalArgument() {
        TSPRequest request = new TSPRequest("dynamic", List.of(new Coordinate(0, 0)), null, null);

        assertThatThrownBy(() -> TSPService.solve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("At least two");
    }

    @Test
    void solve_dynamic_visitsAllNodes() {
        TSPRequest request = new TSPRequest("dynamic", TRIANGLE, null, null);

        TSPResult result = TSPService.solve(request);

        // Path minus the return should contain all node IDs
        List<Integer> visited = result.path().subList(0, result.path().size() - 1);
        assertThat(visited).containsExactlyInAnyOrder(0, 1, 2);
    }

    @Test
    void solve_dynamic_tooManyNodes_throwsIllegalArgument() {
        List<Coordinate> tooMany = IntStream.range(0, TSPService.MAX_NODES_DYNAMIC + 1)
                .mapToObj(i -> new Coordinate(i, i))
                .toList();
        TSPRequest request = new TSPRequest("dynamic", tooMany, null, null);

        assertThatThrownBy(() -> TSPService.solve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at most %d".formatted(TSPService.MAX_NODES_DYNAMIC));
    }

    @Test
    void solve_hillclimb_tooManyNodes_throwsIllegalArgument() {
        List<Coordinate> tooMany = IntStream.range(0, TSPService.MAX_NODES_HILLCLIMB + 1)
                .mapToObj(i -> new Coordinate(i, i))
                .toList();
        TSPRequest request = new TSPRequest("hillclimb", tooMany, 1000, 10);

        assertThatThrownBy(() -> TSPService.solve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at most %d".formatted(TSPService.MAX_NODES_HILLCLIMB));
    }

    @Test
    void solve_hillclimb_tooManyIterations_throwsIllegalArgument() {
        TSPRequest request = new TSPRequest("hillclimb", TRIANGLE, TSPService.MAX_ITERATIONS + 1, 10);

        assertThatThrownBy(() -> TSPService.solve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("iterations");
    }

    @Test
    void solve_hillclimb_tooManyAttempts_throwsIllegalArgument() {
        TSPRequest request = new TSPRequest("hillclimb", TRIANGLE, 1000, TSPService.MAX_ATTEMPTS + 1);

        assertThatThrownBy(() -> TSPService.solve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("maxAttempts");
    }
}