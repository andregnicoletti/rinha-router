package com.nicoletti.rinharouter.service.impl;

import com.nicoletti.rinharouter.dto.PaymentProcessorRequest;
import com.nicoletti.rinharouter.dto.PaymentProcessorResponse;
import com.nicoletti.rinharouter.entity.PaymentTransaction;
import com.nicoletti.rinharouter.repository.PaymentTransactionRepository;
import com.nicoletti.rinharouter.service.api.AnalyzerStatusService;
import com.nicoletti.rinharouter.service.api.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final AnalyzerStatusService analyzerStatusService;

    private final PaymentTransactionRepository repository;

    public PaymentServiceImpl(AnalyzerStatusService analyzerStatusService, PaymentTransactionRepository repository) {
        this.analyzerStatusService = analyzerStatusService;
        this.repository = repository;
    }

    @Override
    public Mono<PaymentProcessorResponse> processPayment(String correlationId, double amount) {

        //TODO: salvar a quantidade de requisições feita e o total de valor processado

        PaymentProcessorRequest paymentProcessorRequest = new PaymentProcessorRequest(correlationId, amount, LocalDateTime.now().toString());
        logger.info("Processing payment request: {}", paymentProcessorRequest);

        return WebClient.builder()
                .baseUrl(analyzerStatusService.getServiceStatus().baseUrl())
                .build()
                .post()
                .uri("/payments")
                .bodyValue(paymentProcessorRequest)
                .exchangeToMono(response -> {

                    boolean success = response.statusCode().is2xxSuccessful();
                    logger.info("Received response from payment service: {}", response.statusCode());

                    if (success) {

                        //salvar a transação no repositório
                        PaymentTransaction transaction = new PaymentTransaction();
                        transaction.setCorrelationId(correlationId);
                        transaction.setAmount(amount);
                        transaction.setEndpointType(analyzerStatusService.getServiceStatus().serviceName());
                        transaction.setProcessedAt(LocalDateTime.now());


                        return Mono.fromCallable(() -> repository.save(transaction))
                                .subscribeOn(Schedulers.boundedElastic())
                                .thenReturn(new PaymentProcessorResponse(true));

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
