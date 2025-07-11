package com.nicoletti.rinharouter.controller;

import com.nicoletti.rinharouter.dto.PaymentRequest;
import com.nicoletti.rinharouter.service.api.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PaymentHandler {

    private static final Logger logger = LoggerFactory.getLogger(PaymentHandler.class);

    private final PaymentService paymentService;

    public PaymentHandler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public Mono<ServerResponse> processPayment(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PaymentRequest.class)
                .flatMap(dto -> paymentService.processPayment(dto.correlationId(), dto.amount()))
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }

}
