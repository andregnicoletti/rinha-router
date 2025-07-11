package com.nicoletti.rinharouter.service.api;

import com.nicoletti.rinharouter.dto.PaymentProcessorResponse;
import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<PaymentProcessorResponse> processPayment(String correlationId, double amount);

}
