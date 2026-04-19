package com.zacharyab24.playground.os.concurrency;

import com.zacharyab24.comp2240.concurrency.monitor.MonitorScheduler;
import com.zacharyab24.comp2240.concurrency.semaphore.SemaphoreScheduler;
import com.zacharyab24.playground.os.concurrency.model.ConcurrencyRequest;
import com.zacharyab24.playground.os.concurrency.model.ConcurrencyTask;
import com.zacharyab24.playground.os.concurrency.model.TaskResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConcurrencyServiceTest {

    private static final List<ConcurrencyTask> TASKS = List.of(
            new ConcurrencyTask("tasks1", 2, List.of(5, 3, 8))
    );

    @Test
    void runSimulation_semaphore_returnsResults() throws InterruptedException {
        ConcurrencyRequest request = new ConcurrencyRequest(TASKS);

        List<TaskResponse> results = ConcurrencyService.runSimulation(request, SemaphoreScheduler::new);

        assertThat(results).isNotEmpty();
        assertThat(results).allSatisfy(r -> {
            assertThat(r.taskFile()).isEqualTo("tasks1");
            assertThat(r.input()).isIn(5, 3, 8);
        });
    }

    @Test
    void runSimulation_monitor_returnsResults() throws InterruptedException {
        ConcurrencyRequest request = new ConcurrencyRequest(TASKS);

        List<TaskResponse> results = ConcurrencyService.runSimulation(request, MonitorScheduler::new);

        assertThat(results).isNotEmpty();
        assertThat(results).allSatisfy(r -> {
            assertThat(r.taskFile()).isEqualTo("tasks1");
            assertThat(r.input()).isIn(5, 3, 8);
        });
    }

    @Test
    void runSimulation_resultContainsSquaredValues() throws InterruptedException {
        ConcurrencyRequest request = new ConcurrencyRequest(
                List.of(new ConcurrencyTask("test", 1, List.of(4)))
        );

        List<TaskResponse> results = ConcurrencyService.runSimulation(request, SemaphoreScheduler::new);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).input()).isEqualTo(4);
        assertThat(results.get(0).result()).isEqualTo(16);
    }

    @Test
    void runSimulation_multipleTasks_combinesResults() throws InterruptedException {
        ConcurrencyRequest request = new ConcurrencyRequest(List.of(
                new ConcurrencyTask("batch1", 1, List.of(2)),
                new ConcurrencyTask("batch2", 1, List.of(3))
        ));

        List<TaskResponse> results = ConcurrencyService.runSimulation(request, SemaphoreScheduler::new);

        assertThat(results).hasSize(2);
        assertThat(results).extracting(TaskResponse::taskFile)
                .containsExactlyInAnyOrder("batch1", "batch2");
    }

    @Test
    void runSimulation_emptyTasks_throwsIllegalArgument() {
        ConcurrencyRequest request = new ConcurrencyRequest(List.of());

        assertThatThrownBy(() -> ConcurrencyService.runSimulation(request, SemaphoreScheduler::new))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("At least one task is required");
    }
}