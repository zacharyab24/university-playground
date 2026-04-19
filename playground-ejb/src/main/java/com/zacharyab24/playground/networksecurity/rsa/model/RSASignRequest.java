package com.zacharyab24.playground.networksecurity.rsa.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to sign a message with an RSA private key.")
public record RSASignRequest(
        @Schema(description = "Message to sign.", example = "Hello, World!",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String message,
        @Schema(description = "RSA private key to sign with.", requiredMode = Schema.RequiredMode.REQUIRED)
        RSAPrivateKeyDto privateKey
) {}