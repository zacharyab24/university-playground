package com.zacharyab24.playground.os.scheduler;

import com.zacharyab24.comp2240.scheduling.algorithms.FCFS;
import com.zacharyab24.comp2240.scheduling.algorithms.PP;
import com.zacharyab24.comp2240.scheduling.algorithms.PRR;
import com.zacharyab24.comp2240.scheduling.algorithms.SPN;
import com.zacharyab24.comp2240.scheduling.model.Process;
import com.zacharyab24.playground.os.scheduler.model.ProcessDto;
import com.zacharyab24.playground.os.scheduler.model.SchedulerRequest;
import com.zacharyab24.playground.os.scheduler.model.SchedulerResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a port of A1.java from COMP2240, but redesigned to be consumed by a rest service
 * The function of this program is to simulate scheduling algorithms for a list of processes provided in a text file.
 * The scheduling algorithms implemented are:
 * <ul>
 * <li>First Come, First Served</li>
 * <li>Shortest Process Next</li>
 * <li>Preemptive Priority</li>
 * <li>Priority Round Robin</li>
 * </ul>
 */
public class SchedulerService {
    /**
     * Runs the 4 scheduling algorithms
     *
     * @param request the dispatcher overhead and process list
     * @return per-algorithm summaries (turnaround, waiting time)
     */
    public static List<SchedulerResponse> runScheduler(SchedulerRequest request) {
        ArrayList<Process> processes = request.processes().stream()
                .map(ProcessDto::toProcess)
                .collect(Collectors.toCollection(ArrayList::new));

        int dispatcher = request.dispatcher();
        return List.of(
                SchedulerResponse.fromResult(new FCFS(dispatcher, processes).run()),
                SchedulerResponse.fromResult(new SPN(dispatcher, processes).run()),
                SchedulerResponse.fromResult(new PP(dispatcher, processes).run()),
                SchedulerResponse.fromResult(new PRR(dispatcher, processes).run())
        );
    }
}
