package com.zacharyab24.playground.datastructures.tolls.model;

// TODO: Add @Schema annotations
// Maps to EToll(license, type, charge)
public record TollRecord(
        String license,
        String type,
        double charge
) {}