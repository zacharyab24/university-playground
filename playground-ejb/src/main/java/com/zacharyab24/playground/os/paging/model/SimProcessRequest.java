package com.zacharyab24.playground.os.paging.model;

import com.zacharyab24.comp2240.paging.model.SimProcess;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "A single process for the paging simulation.")
public record SimProcessRequest(
        @Schema(description = "Process ID.", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        int pid,
        @Schema(description = "Process name.", example = "Process1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String name,
        @Schema(description = "Page reference string.", example = "[1, 2, 3, 1, 4]",
                requiredMode = Schema.RequiredMode.REQUIRED)
        List<Integer> pages
) {
    public SimProcess toSimProcess() {
        return new SimProcess(pid, name, new ArrayList<>(pages));
    }
}
