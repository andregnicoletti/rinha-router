package com.nicoletti.rinharouter.service.impl;

import com.nicoletti.rinharouter.dto.PaymentProcessorRequest;
import com.nicoletti.rinharouter.dto.PaymentRequest;
import com.nicoletti.rinharouter.dto.PaymentProcessorResponse;
import com.nicoletti.rinharouter.service.api.AnalyzerStatusService;
import com.nicoletti.rinharouter.service.api.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final AnalyzerStatusService analyzerStatusService;

    public PaymentServiceImpl(AnalyzerStatusService analyzerStatusService) {
        this.analyzerStatusService = analyzerStatusService;
    }

    @Override
    public Mono<PaymentProcessorResponse> processPayment(String correlationId, double amount) {

        //TODO: decidir qual serviço será chamado
        //TODO: fazer a requisição post para o serviço de pagamento
        //TODO: salvar a quantidade de requisições feita e o total de valor processado

        PaymentProcessorRequest paymentProcessorRequest = new PaymentProcessorRequest(correlationId, amount, LocalDateTime.now().toString());
        logger.info("Processing payment request: {}", paymentProcessorRequest);

        String baseUrl = analyzerStatusService.getCurrentBaseUrl();

        return WebClient.builder()
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("/payments")
                .bodyValue(paymentProcessorRequest)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        logger.info("Payment processed successfully: {}", response.statusCode());
                        return Mono.just(new PaymentProcessorResponse(true));
                    } else {
                        logger.error("Failed to process payment: {}", response.statusCode());
                        return Mono.just(new PaymentProcessorResponse(false));
                    }
                })
                .onErrorResume(ex -> {
                    logger.error("Erro ao chamar o serviço de pagamento", ex);
                    return Mono.just(new PaymentProcessorResponse(false));
                });

    }

}
