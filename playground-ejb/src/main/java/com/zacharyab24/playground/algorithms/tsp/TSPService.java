package com.zacharyab24.playground.algorithms.tsp;

import com.zacharyab24.comp2230.tsp.algorithm.Algorithm;
import com.zacharyab24.comp2230.tsp.algorithm.AlgorithmEnum;
import com.zacharyab24.comp2230.tsp.algorithm.AlgorithmFactory;
import com.zacharyab24.comp2230.tsp.io.TSPReader;
import com.zacharyab24.comp2230.tsp.model.Coordinate;
import com.zacharyab24.comp2230.tsp.model.TSPInstance;
import com.zacharyab24.comp2230.tsp.model.TSPResult;
import com.zacharyab24.playground.algorithms.tsp.model.TSPRequest;

public class TSPService {

    static final int MAX_NODES_DYNAMIC = 20;
    static final int MAX_NODES_HILLCLIMB = 100;
    static final int MAX_ITERATIONS = 10_000_000;
    static final int MAX_ATTEMPTS = 1_000;

    public static TSPResult solve(TSPRequest request) {
        if (request.coordinates() == null || request.coordinates().size() < 2) {
            throw new IllegalArgumentException("At least two coordinates are required");
        }

        AlgorithmEnum type = AlgorithmEnum.fromString(request.algorithm());
        validateLimits(type, request);

        Coordinate[] coords = request.coordinates().toArray(Coordinate[]::new);
        TSPInstance instance = TSPReader.fromCoordinates(coords);
        String[] args = buildArgs(type, request);

        Algorithm algorithm = AlgorithmFactory.create(type, instance, args);
        return algorithm.solve();
    }

    private static void validateLimits(AlgorithmEnum type, TSPRequest request) {
        int nodeCount = request.coordinates().size();
        switch (type) {
            case DYNAMIC -> {
                if (nodeCount > MAX_NODES_DYNAMIC) {
                    throw new IllegalArgumentException(
                            "Dynamic programming supports at most %d nodes (got %d)"
                                    .formatted(MAX_NODES_DYNAMIC, nodeCount));
                }
            }
            case HILLCLIMB -> {
                if (nodeCount > MAX_NODES_HILLCLIMB) {
                    throw new IllegalArgumentException(
                            "Hill climb supports at most %d nodes (got %d)"
                                    .formatted(MAX_NODES_HILLCLIMB, nodeCount));
                }
                if (request.iterations() != null && request.iterations() > MAX_ITERATIONS) {
                    throw new IllegalArgumentException(
                            "iterations must not exceed %d (got %d)"
                                    .formatted(MAX_ITERATIONS, request.iterations()));
                }
                if (request.maxAttempts() != null && request.maxAttempts() > MAX_ATTEMPTS) {
                    throw new IllegalArgumentException(
                            "maxAttempts must not exceed %d (got %d)"
                                    .formatted(MAX_ATTEMPTS, request.maxAttempts()));
                }
            }
        }
    }

    private static String[] buildArgs(AlgorithmEnum type, TSPRequest request) {
        return switch (type) {
            case DYNAMIC -> new String[]{};
            case HILLCLIMB -> {
                if (request.iterations() == null || request.maxAttempts() == null) {
                    throw new IllegalArgumentException("iterations and maxAttempts are required for hillclimb");
                }
                yield new String[]{
                        String.valueOf(request.iterations()),
                        String.valueOf(request.maxAttempts())
                };
            }
        };
    }
}