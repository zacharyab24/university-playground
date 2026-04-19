package com.zacharyab24.playground.networksecurity.rsa.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "RSA private key components.")
public record RSAPrivateKeyDto(
        @Schema(description = "First prime factor (p) as decimal string.")
        String prime1,
        @Schema(description = "Second prime factor (q) as decimal string.")
        String prime2,
        @Schema(description = "Private exponent (d) as decimal string.")
        String exponent
) {}