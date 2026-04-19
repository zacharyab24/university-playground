package com.zacharyab24.playground.networksecurity.cbc.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Generated AES key and initialization vector.")
public record CBCKeyResponse(
        @Schema(description = "AES key (base64-encoded).")
        String key,
        @Schema(description = "Initialization vector (base64-encoded).")
        String iv
) {}