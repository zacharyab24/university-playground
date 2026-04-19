package com.zacharyab24.playground.networksecurity.rsa.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "RSA public key components.")
public record RSAPublicKeyDto(
        @Schema(description = "Modulus (n = p * q) as decimal string.", example = "123456789...")
        String modulus,
        @Schema(description = "Public exponent.", example = "65537")
        String exponent
) {}