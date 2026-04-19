package com.zacharyab24.playground.networksecurity.hmac.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to compute an HMAC-SHA256.")
public record HMACRequest(
        @Schema(description = "Secret key for the HMAC.", example = "mysecretkey",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String key,
        @Schema(description = "Message to authenticate.", example = "Hello, World!",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String message
) {}