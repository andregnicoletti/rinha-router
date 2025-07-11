package com.nicoletti.rinharouter.dto;

public record PaymentSummary(
        String endpointType,
        long totalRequests,
        double totalAmount
) {
}
