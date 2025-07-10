package com.nicoletti.rinharouter.dto;

import java.time.LocalDateTime;

public record ExtractResponse(
        int saldo,
        LocalDateTime dataExtrato,
        int limite
) {
}
