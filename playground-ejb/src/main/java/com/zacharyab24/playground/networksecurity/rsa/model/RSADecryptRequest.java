package com.zacharyab24.playground.networksecurity.rsa.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to decrypt a ciphertext with an RSA private key.")
public record RSADecryptRequest(
        @Schema(description = "Ciphertext to decrypt (decimal string).", example = "987654321...",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String ciphertext,
        @Schema(description = "RSA private key to decrypt with.", requiredMode = Schema.RequiredMode.REQUIRED)
        RSAPrivateKeyDto privateKey
) {}