package com.zacharyab24.playground.os.scheduler;

import com.zacharyab24.playground.os.scheduler.model.ProcessDto;
import com.zacharyab24.playground.os.scheduler.model.SchedulerRequest;
import com.zacharyab24.playground.os.scheduler.model.SchedulerResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SchedulerServiceTest {

    private static final List<ProcessDto> PROCESSES = List.of(
            new ProcessDto("p1", 0, 10, 0),
            new ProcessDto("p2", 0, 1, 2),
            new ProcessDto("p3", 2, 4, 0),
            new ProcessDto("p4", 0, 1, 1),
            new ProcessDto("p5", 0, 5, 5)
    );

    @Test
    void runScheduler_returnsFourAlgorithms() {
        SchedulerRequest request = new SchedulerRequest(1, PROCESSES);

        List<SchedulerResponse> results = SchedulerService.runScheduler(request);

        assertThat(results).hasSize(4);
        assertThat(results).extracting(SchedulerResponse::processorID)
                .containsExactly("FCFS", "SPN", "PP", "PRR");
    }

    @Test
    void runScheduler_allResultsHaveNonEmptyFields() {
        SchedulerRequest request = new SchedulerRequest(1, PROCESSES);

        List<SchedulerResponse> results = SchedulerService.runScheduler(request);

        for (SchedulerResponse result : results) {
            assertThat(result.processorID()).isNotBlank();
            assertThat(result.turnAround()).isNotBlank();
            assertThat(result.waitingTime()).isNotBlank();
        }
    }

    @Test
    void runScheduler_spnHasLowerTurnaroundThanFcfs() {
        SchedulerRequest request = new SchedulerRequest(1, PROCESSES);

        List<SchedulerResponse> results = SchedulerService.runScheduler(request);

        double fcfsTurnaround = Double.parseDouble(results.get(0).turnAround());
        double spnTurnaround = Double.parseDouble(results.get(1).turnAround());
        assertThat(spnTurnaround).isLessThanOrEqualTo(fcfsTurnaround);
    }

    @Test
    void runScheduler_singleProcess() {
        List<ProcessDto> single = List.of(new ProcessDto("p1", 0, 5, 0));
        SchedulerRequest request = new SchedulerRequest(1, single);

        List<SchedulerResponse> results = SchedulerService.runScheduler(request);

        assertThat(results).hasSize(4);
        for (SchedulerResponse result : results) {
            double turnaround = Double.parseDouble(result.turnAround());
            assertThat(turnaround).isGreaterThan(0);
        }
    }

    @Test
    void runScheduler_zeroDispatcherOverhead() {
        SchedulerRequest request = new SchedulerRequest(0, PROCESSES);

        List<SchedulerResponse> results = SchedulerService.runScheduler(request);

        assertThat(results).hasSize(4);
        for (SchedulerResponse result : results) {
            double turnaround = Double.parseDouble(result.turnAround());
            assertThat(turnaround).isGreaterThan(0);
        }
    }
}