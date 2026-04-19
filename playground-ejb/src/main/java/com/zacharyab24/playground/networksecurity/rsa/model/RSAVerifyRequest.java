package com.zacharyab24.playground.networksecurity.rsa.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to verify an RSA signature.")
public record RSAVerifyRequest(
        @Schema(description = "Original message.", example = "Hello, World!",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String message,
        @Schema(description = "Signature to verify (decimal string).",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String signature,
        @Schema(description = "RSA public key to verify with.", requiredMode = Schema.RequiredMode.REQUIRED)
        RSAPublicKeyDto publicKey
) {}