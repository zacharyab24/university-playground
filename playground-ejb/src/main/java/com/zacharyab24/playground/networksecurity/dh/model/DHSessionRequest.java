package com.zacharyab24.playground.networksecurity.dh.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to derive a shared session key from DH key exchange.")
public record DHSessionRequest(
        @Schema(description = "The other party's DH public key (decimal string).",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String remotePublicKey,
        @Schema(description = "Your DH private key (decimal string).",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String localPrivateKey
) {}