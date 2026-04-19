package com.zacharyab24.playground.networksecurity.rsa.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to encrypt a message with an RSA public key.")
public record RSAEncryptRequest(
        @Schema(description = "Plaintext message to encrypt.", example = "Hello, World!",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String plaintext,
        @Schema(description = "RSA public key to encrypt with.", requiredMode = Schema.RequiredMode.REQUIRED)
        RSAPublicKeyDto publicKey
) {}