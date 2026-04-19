package com.zacharyab24.playground.os.paging;

import com.zacharyab24.comp2240.paging.model.RRSResults;
import com.zacharyab24.comp2240.paging.model.SimProcess;
import com.zacharyab24.playground.os.paging.model.PagingRequest;
import com.zacharyab24.playground.os.paging.model.PagingResponse;
import com.zacharyab24.playground.os.paging.model.SimProcessRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PagingServiceTest {

    private static final List<SimProcessRequest> PROCESSES = List.of(
            new SimProcessRequest(1, "Process1", List.of(1, 2, 3, 1, 4, 2, 5)),
            new SimProcessRequest(2, "Process2", List.of(2, 3, 4, 2, 1, 3, 5))
    );

    @Test
    void runPagingSimulation_returnsTwoPolicies() {
        PagingRequest request = new PagingRequest(PROCESSES, 30, 3);

        PagingResponse response = PagingService.runPagingSimulation(request);

        assertThat(response.results()).hasSize(2);
    }

    @Test
    void runPagingSimulation_containsBothAlgorithms() {
        PagingRequest request = new PagingRequest(PROCESSES, 30, 3);

        PagingResponse response = PagingService.runPagingSimulation(request);

        assertThat(response.results())
                .extracting(RRSResults::algorithm)
                .containsExactly("Fixed-Local", "Variable-Global");
    }

    @Test
    void runPagingSimulation_eachPolicyHasAllProcesses() {
        PagingRequest request = new PagingRequest(PROCESSES, 30, 3);

        PagingResponse response = PagingService.runPagingSimulation(request);

        for (RRSResults result : response.results()) {
            assertThat(result.processes()).hasSize(2);
        }
    }

    @Test
    void runPagingSimulation_processesHaveFaults() {
        PagingRequest request = new PagingRequest(PROCESSES, 30, 3);

        PagingResponse response = PagingService.runPagingSimulation(request);

        for (RRSResults result : response.results()) {
            assertThat(result.processes()).allSatisfy(p -> {
                assertThat(p.faults()).isGreaterThan(0);
                assertThat(p.finishTime()).isGreaterThan(0);
            });
        }
    }

    @Test
    void toSimProcesses_convertsAllDtos() {
        ArrayList<SimProcess> result = PagingService.toSimProcesses(PROCESSES);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPid()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("Process1");
        assertThat(result.get(0).getPages()).containsExactly(1, 2, 3, 1, 4, 2, 5);
    }

    @Test
    void toSimProcesses_producesIndependentCopies() {
        ArrayList<SimProcess> first = PagingService.toSimProcesses(PROCESSES);
        ArrayList<SimProcess> second = PagingService.toSimProcesses(PROCESSES);

        // Different instances
        assertThat(first.get(0)).isNotSameAs(second.get(0));
        // But same data
        assertThat(first.get(0).getPid()).isEqualTo(second.get(0).getPid());
    }
}