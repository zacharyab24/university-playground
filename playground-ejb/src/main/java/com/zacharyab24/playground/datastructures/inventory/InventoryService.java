package com.zacharyab24.playground.datastructures.inventory;

import com.zacharyab24.playground.datastructures.inventory.model.BenchmarkRequest;
import com.zacharyab24.playground.datastructures.inventory.model.BenchmarkResponse;
import com.zacharyab24.playground.datastructures.inventory.model.InventoryStatsRequest;
import com.zacharyab24.playground.datastructures.inventory.model.InventoryStatsResponse;
import com.zacharyab24.playground.datastructures.inventory.model.PartRecord;
import com.zacharyab24.seng1120.hashtree.model.BSTree;
import com.zacharyab24.seng1120.hashtree.model.DataStructure;
import com.zacharyab24.seng1120.hashtree.model.HTable;
import com.zacharyab24.seng1120.hashtree.model.MechPart;
import com.zacharyab24.seng1120.hashtree.stats.BSTreeStatsImpl;
import com.zacharyab24.seng1120.hashtree.stats.HTableStatsImpl;
import com.zacharyab24.seng1120.hashtree.stats.InventoryStats;

import java.util.List;

public class InventoryService {

    private static final int TABLE_SIZE = 5000;
    static final int MAX_BENCHMARK_ITERATIONS = 1_000_000;

    /**
     * Builds the chosen data structure from the request parts and calculates inventory statistics.
     *
     * @param request the data structure type, parts list, and less-than threshold
     * @return statistics including part count, total inventory, and parts below the threshold
     */
    public static InventoryStatsResponse getStats(InventoryStatsRequest request) {
        List<MechPart> inventory = createInventory(request.parts());
        DataStructure<MechPart> ds;
        InventoryStats stats;

        switch (request.dataStructure()) {
            case BSTREE -> {
                BSTree<MechPart> tree = new BSTree<>();
                inventory.forEach(tree::add);
                ds = tree;
                stats = new BSTreeStatsImpl(tree);
            }
            case HTABLE -> {
                HTable<MechPart> table = new HTable<>(TABLE_SIZE);
                inventory.forEach(table::add);
                ds = table;
                stats = new HTableStatsImpl(table);
            }
            default -> throw new IllegalArgumentException("Unsupported data structure");
        }

        return new InventoryStatsResponse(
                request.dataStructure(),
                stats.calculateParts(),
                stats.calculateInventory(),
                stats.calculateLessThan(request.lessThanThreshold()),
                ds.toString()
        );
    }

    /**
     * Benchmarks add/remove cycles on the chosen data structure.
     * Each iteration removes all parts then adds them back.
     *
     * @param request the data structure type, parts list, and number of iterations
     * @return timing results including total time and time per operation
     */
    public static BenchmarkResponse benchmark(BenchmarkRequest request) {
        if (request.iterations() < 1 || request.iterations() > MAX_BENCHMARK_ITERATIONS) {
            throw new IllegalArgumentException(
                    "iterations must be between 1 and %d".formatted(MAX_BENCHMARK_ITERATIONS));
        }

        List<MechPart> inventory = createInventory(request.parts());
        DataStructure<MechPart> ds = switch (request.dataStructure()) {
            case BSTREE -> {
                BSTree<MechPart> tree = new BSTree<>();
                inventory.forEach(tree::add);
                yield tree;
            }
            case HTABLE -> {
                HTable<MechPart> table = new HTable<>(TABLE_SIZE);
                inventory.forEach(table::add);
                yield table;
            }
        };

        long startNanos = System.nanoTime();
        for (int j = 0; j < request.iterations(); j++) {
            for (MechPart part : inventory) {
                ds.remove(part);
            }
            for (MechPart part : inventory) {
                ds.add(part);
            }
        }
        long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000;
        int totalOps = request.iterations() * inventory.size() * 2;

        return new BenchmarkResponse(
                request.dataStructure(),
                totalOps,
                elapsedMs,
                totalOps > 0 ? (double) elapsedMs / totalOps : 0
        );
    }

    /**
     * Converts request DTOs to MechPart instances.
     *
     * @param parts the request part records
     * @return a list of MechPart instances
     */
    protected static List<MechPart> createInventory(List<PartRecord> parts) {
        return parts.stream()
                .map(p -> new MechPart(p.code(), p.quantity()))
                .toList();
    }

    /**
     * Builds a BSTree from the inventory and wraps it in a stats implementation.
     *
     * @param inventory the parts to insert into the tree
     * @return a BSTreeStatsImpl backed by the populated tree
     */
    protected static BSTreeStatsImpl createBSTreeInstance(List<MechPart> inventory) {
        BSTree<MechPart> tree = new BSTree<>();
        for (MechPart part : inventory) {
            tree.add(part);
        }
        return new BSTreeStatsImpl(tree);
    }

    /**
     * Builds an HTable from the inventory and wraps it in a stats implementation.
     *
     * @param inventory the parts to insert into the hash table
     * @return an HTableStatsImpl backed by the populated table
     */
    protected static HTableStatsImpl createHTableInstance(List<MechPart> inventory) {
        HTable<MechPart> table = new HTable<>(TABLE_SIZE);
        for (MechPart part : inventory) {
            table.add(part);
        }
        return new HTableStatsImpl(table);
    }
}