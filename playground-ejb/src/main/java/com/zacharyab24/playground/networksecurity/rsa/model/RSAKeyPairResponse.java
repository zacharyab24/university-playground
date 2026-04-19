package com.zacharyab24.playground.networksecurity.rsa.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Generated RSA key pair.")
public record RSAKeyPairResponse(
        @Schema(description = "RSA public key.")
        RSAPublicKeyDto publicKey,
        @Schema(description = "RSA private key.")
        RSAPrivateKeyDto privateKey
) {}