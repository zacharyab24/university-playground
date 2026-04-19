package com.zacharyab24.playground.os.paging;

import com.zacharyab24.comp2240.paging.memory.FixedLocalReplacementPolicy;
import com.zacharyab24.comp2240.paging.memory.ReplacementPolicy;
import com.zacharyab24.comp2240.paging.memory.VariableGlobalReplacementPolicy;
import com.zacharyab24.comp2240.paging.model.RRSResults;
import com.zacharyab24.comp2240.paging.model.SimProcess;
import com.zacharyab24.comp2240.paging.scheduler.RoundRobinScheduler;
import com.zacharyab24.playground.os.paging.model.PagingRequest;
import com.zacharyab24.playground.os.paging.model.PagingResponse;
import com.zacharyab24.playground.os.paging.model.SimProcessRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PagingService {
    public static PagingResponse runPagingSimulation(PagingRequest request) {
        ArrayList<SimProcess> falrProcesses = toSimProcesses(request.processes());
        ArrayList<SimProcess> vagrProcesses = toSimProcesses(request.processes());
        return new PagingResponse(List.of(
                runSimulation(request.quantum(), falrProcesses, new FixedLocalReplacementPolicy(request.totalFrames(), falrProcesses)),
                runSimulation(request.quantum(), vagrProcesses, new VariableGlobalReplacementPolicy(request.totalFrames()))
        ));
    }

    /**
     * Converts request DTOs to SimProcess instances.
     * Each call produces a fresh set since simulation runs mutate their processes.
     *
     * @param dtos the request DTOs
     * @return a new list of SimProcess instances
     */
    static ArrayList<SimProcess> toSimProcesses(List<SimProcessRequest> dtos) {
        return dtos.stream()
                .map(SimProcessRequest::toSimProcess)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Runs a scheduling simulation with the given quantum, processes and policy.
     *
     * @param quantum   the quantum for the scheduling algorithm
     * @param processes the list of processes to schedule
     * @param policy    the memory replacement policy to use
     * @return the structured simulation results
     */
    private static RRSResults runSimulation(int quantum, ArrayList<SimProcess> processes, ReplacementPolicy policy) {
        return new RoundRobinScheduler(quantum, processes, policy).run();
    }
}
