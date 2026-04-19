package com.zacharyab24.playground.networksecurity.dh.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Derived shared session key from Diffie-Hellman exchange.")
public record DHSessionResponse(
        @Schema(description = "Shared session key (decimal string).")
        String sessionKey,
        @Schema(description = "SHA-256 hash of the session key (hex string).")
        String hashedSessionKey
) {}