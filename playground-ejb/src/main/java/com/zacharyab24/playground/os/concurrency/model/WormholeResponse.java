package com.zacharyab24.playground.os.concurrency.model;

import com.zacharyab24.comp2240.concurrency.wormhole.CrossingResult;
import com.zacharyab24.comp2240.concurrency.wormhole.WormHole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Results of a wormhole traveller simulation.")
public record WormholeResponse(
        @Schema(description = "Total number of wormhole crossings completed.", example = "6")
        int totalCrossings,
        @Schema(description = "Ordered list of individual crossing results.")
        List<CrossingResult> crossings
) {
    public static WormholeResponse fromWormhole(WormHole wormhole) {
        return new WormholeResponse(wormhole.getCount(), wormhole.getCrossings());
    }
}
