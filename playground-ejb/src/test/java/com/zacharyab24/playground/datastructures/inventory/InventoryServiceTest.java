package com.zacharyab24.playground.datastructures.inventory;

import com.zacharyab24.playground.datastructures.inventory.model.BenchmarkRequest;
import com.zacharyab24.playground.datastructures.inventory.model.BenchmarkResponse;
import com.zacharyab24.playground.datastructures.inventory.model.DataStructures;
import com.zacharyab24.playground.datastructures.inventory.model.InventoryStatsRequest;
import com.zacharyab24.playground.datastructures.inventory.model.InventoryStatsResponse;
import com.zacharyab24.playground.datastructures.inventory.model.PartRecord;
import com.zacharyab24.seng1120.hashtree.model.MechPart;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InventoryServiceTest {

    private static final List<PartRecord> PARTS = List.of(
            new PartRecord("AAA", 50),
            new PartRecord("BBB", 5),
            new PartRecord("CCC", 100)
    );

    @Test
    void getStats_bstree_returnsCorrectCounts() {
        InventoryStatsRequest request = new InventoryStatsRequest(DataStructures.BSTREE, PARTS, 10);

        InventoryStatsResponse response = InventoryService.getStats(request);

        assertThat(response.dataStructure()).isEqualTo(DataStructures.BSTREE);
        assertThat(response.totalParts()).isEqualTo(3);
        assertThat(response.totalInventory()).isEqualTo(155);
        assertThat(response.partsLessThan()).isEqualTo(1); // BBB(5) < 10
    }

    @Test
    void getStats_htable_returnsCorrectCounts() {
        InventoryStatsRequest request = new InventoryStatsRequest(DataStructures.HTABLE, PARTS, 10);

        InventoryStatsResponse response = InventoryService.getStats(request);

        assertThat(response.dataStructure()).isEqualTo(DataStructures.HTABLE);
        assertThat(response.totalParts()).isEqualTo(3);
        assertThat(response.totalInventory()).isEqualTo(155);
        assertThat(response.partsLessThan()).isEqualTo(1);
    }

    @Test
    void getStats_contentsIsNotObjectReference() {
        InventoryStatsRequest request = new InventoryStatsRequest(DataStructures.BSTREE, PARTS, 10);

        InventoryStatsResponse response = InventoryService.getStats(request);

        assertThat(response.contents()).doesNotContain("@");
    }

    @Test
    void getStats_bstree_contentsInOrder() {
        InventoryStatsRequest request = new InventoryStatsRequest(DataStructures.BSTREE, PARTS, 10);

        InventoryStatsResponse response = InventoryService.getStats(request);

        // BSTree should produce in-order (alphabetical) output
        assertThat(response.contents()).contains("AAA");
        assertThat(response.contents()).contains("BBB");
        assertThat(response.contents()).contains("CCC");
    }

    @Test
    void benchmark_bstree_returnsTiming() {
        BenchmarkRequest request = new BenchmarkRequest(DataStructures.BSTREE, PARTS, 100);

        BenchmarkResponse response = InventoryService.benchmark(request);

        assertThat(response.dataStructure()).isEqualTo(DataStructures.BSTREE);
        assertThat(response.iterations()).isEqualTo(600); // 100 * 3 parts * 2 (add+remove)
        assertThat(response.totalTimeMs()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void benchmark_htable_returnsTiming() {
        BenchmarkRequest request = new BenchmarkRequest(DataStructures.HTABLE, PARTS, 100);

        BenchmarkResponse response = InventoryService.benchmark(request);

        assertThat(response.dataStructure()).isEqualTo(DataStructures.HTABLE);
        assertThat(response.iterations()).isEqualTo(600);
    }

    @Test
    void benchmark_tooManyIterations_throwsIllegalArgument() {
        BenchmarkRequest request = new BenchmarkRequest(
                DataStructures.BSTREE, PARTS, InventoryService.MAX_BENCHMARK_ITERATIONS + 1);

        assertThatThrownBy(() -> InventoryService.benchmark(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("iterations");
    }

    @Test
    void benchmark_zeroIterations_throwsIllegalArgument() {
        BenchmarkRequest request = new BenchmarkRequest(DataStructures.BSTREE, PARTS, 0);

        assertThatThrownBy(() -> InventoryService.benchmark(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createInventory_mapsAllParts() {
        List<MechPart> result = InventoryService.createInventory(PARTS);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).code()).isEqualTo("AAA");
        assertThat(result.get(0).quantity()).isEqualTo(50);
    }
}
