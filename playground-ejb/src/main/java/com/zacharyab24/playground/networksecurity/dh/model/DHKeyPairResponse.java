package com.zacharyab24.playground.networksecurity.dh.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Generated Diffie-Hellman key pair.")
public record DHKeyPairResponse(
        @Schema(description = "DH private key (decimal string). Keep secret.")
        String privateKey,
        @Schema(description = "DH public key (decimal string). Share with the other party.")
        String publicKey
) {}