package com.zacharyab24.playground.networksecurity.cbc.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to generate an AES key.")
public record CBCKeyRequest(
        @Schema(description = "Key size in bits.", example = "256",
                allowableValues = {"128", "192", "256"},
                requiredMode = Schema.RequiredMode.REQUIRED)
        int keySize
) {}