package com.zacharyab24.playground.networksecurity.cbc.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to decrypt a ciphertext with AES-CBC.")
public record CBCDecryptRequest(
        @Schema(description = "Ciphertext to decrypt (base64-encoded).",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String ciphertext,
        @Schema(description = "AES key (base64-encoded).", requiredMode = Schema.RequiredMode.REQUIRED)
        String key,
        @Schema(description = "Initialization vector (base64-encoded).", requiredMode = Schema.RequiredMode.REQUIRED)
        String iv
) {}