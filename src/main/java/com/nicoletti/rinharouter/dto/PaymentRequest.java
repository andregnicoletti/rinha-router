package com.nicoletti.rinharouter.dto;

public record PaymentRequest(
        String correlationId,
        double amount
) {
}
