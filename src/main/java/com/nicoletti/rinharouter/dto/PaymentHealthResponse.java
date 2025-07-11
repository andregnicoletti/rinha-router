package com.nicoletti.rinharouter.dto;

public record PaymentHealthResponse(
        boolean falling,
        int minResponseTime
) {
}
