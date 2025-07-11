package com.nicoletti.rinharouter.controller;

import com.nicoletti.rinharouter.dto.PaymentSummary;
import com.nicoletti.rinharouter.repository.PaymentTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentSummaryHandler {

    private static final Logger logger = LoggerFactory.getLogger(PaymentSummaryHandler.class);

    private final PaymentTransactionRepository repository;

    public PaymentSummaryHandler(PaymentTransactionRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> getSummary(ServerRequest request) {
        String fromParam = request.queryParam("from").orElse(null);
        String toParam = request.queryParam("to").orElse(null);

        if (fromParam == null || toParam == null) {
            return ServerResponse.badRequest().bodyValue("Parâmetros 'from' e 'to' são obrigatórios.");
        }

        LocalDateTime from = LocalDateTime.parse(fromParam);
        LocalDateTime to = LocalDateTime.parse(toParam);

        return Mono.fromCallable(() -> repository.summarizeByEndpoint(from, to))
                .subscribeOn(Schedulers.boundedElastic())
                .map(list -> list.stream().collect(Collectors.toMap(
                        PaymentSummary::endpointType,
                        Function.identity()
                )))
                .flatMap(summaryMap -> ServerResponse.ok().bodyValue(summaryMap));
    }
}
