package com.nicoletti.rinharouter.dto;

public record PaymentProcessorRequest(
        String correlationId,
        double amount,
        String requestedAt
) {
}
