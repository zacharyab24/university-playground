package com.zacharyab24.playground.networksecurity.cbc.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to encrypt a message with AES-CBC.")
public record CBCEncryptRequest(
        @Schema(description = "Plaintext message to encrypt.", example = "Hello, World!",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String plaintext,
        @Schema(description = "AES key (base64-encoded).", requiredMode = Schema.RequiredMode.REQUIRED)
        String key,
        @Schema(description = "Initialization vector (base64-encoded).", requiredMode = Schema.RequiredMode.REQUIRED)
        String iv
) {}