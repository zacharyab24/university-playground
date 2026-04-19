package com.zacharyab24.playground.os.concurrency;

import com.zacharyab24.comp2240.concurrency.common.AbstractScheduler;
import com.zacharyab24.comp2240.concurrency.common.SchedulerFactory;
import com.zacharyab24.playground.os.concurrency.model.ConcurrencyRequest;
import com.zacharyab24.playground.os.concurrency.model.ConcurrencyTask;
import com.zacharyab24.playground.os.concurrency.model.TaskResponse;

import java.util.ArrayList;
import java.util.List;

public class ConcurrencyService {
    public static List<TaskResponse> runSimulation(ConcurrencyRequest request, SchedulerFactory schedulerFactory)
            throws InterruptedException {
        if (request.tasks().isEmpty()) {
            throw new IllegalArgumentException("At least one task is required");
        }

        List<AbstractScheduler> scheduledThreads = new ArrayList<>();
        for (ConcurrencyTask task : request.tasks()) {
            AbstractScheduler scheduler = schedulerFactory.create(
                    task.name(),
                    task.numProcesses(),
                    new ArrayList<>(task.inputs()));
            scheduledThreads.add(scheduler);
            scheduler.start();
        }

        for (AbstractScheduler scheduler : scheduledThreads) {
            scheduler.join();
        }

        return scheduledThreads.stream()
                .flatMap(s -> s.getResults().stream()
                        .map(r -> TaskResponse.fromTaskResult(s.getFileName(), r)))
                .toList();
    }
}
