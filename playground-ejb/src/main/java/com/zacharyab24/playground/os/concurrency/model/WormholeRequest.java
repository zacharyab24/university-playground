package com.zacharyab24.playground.os.concurrency.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Input parameters for the wormhole traveller simulation.")
public record WormholeRequest(
        @Schema(description = "Number of Earth human travellers.", example = "2")
        int earthHumans,
        @Schema(description = "Number of Proxima-b alien travellers.", example = "2")
        int proximaAliens,
        @Schema(description = "Number of wormhole crossings each traveller makes.", example = "3")
        int trips
) {}

