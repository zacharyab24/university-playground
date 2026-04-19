package com.zacharyab24.playground.os.paging.model;

import com.zacharyab24.comp2240.paging.model.RRSResults;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Results of a paging simulation, one entry per replacement policy.")
public record PagingResponse(
        @Schema(description = "Results for each replacement policy (Fixed-Local, Variable-Global).")
        List<RRSResults> results
) {}

